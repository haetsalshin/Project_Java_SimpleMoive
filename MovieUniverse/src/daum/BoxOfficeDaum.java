package daum;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BoxOfficeDaum {
	String baseUrl ="http://ticket2.movie.daum.net/Movie/MovieRankList.aspx";
	int finalCnt = 0;  
	
	public String[][] daumMovieRank (String[][] mvRank) throws IOException{
		
		    // 수집을 멈추기위한 변수(1~10위까지 완료)
		
		Document doc = Jsoup.connect(baseUrl).get();	
		Elements movieList = doc.select("div.desc_boxthumb > strong.tit_join > a");
		
		for(Element movie : movieList) {
			
			if(finalCnt == 10) {
				break;
			}
			// 제목을 가져와야한다.
			String title = movie.text();
			int flag = 999; 
			for (int i = 0; i < mvRank.length; i++) {
				if(mvRank[i][1].equals(title)) {
					flag = i; 
					break;
				}
				
				if( flag == 999) {
					continue; 
				}
			
			
			
			String url = movie.attr("href"); 
			Document movieDoc = Jsoup.connect(url).get(); 
			
			//상세영화 페이지가 없는 영화는 삭제
			if(movieDoc.select("span.txt_name").size() ==0 ) {
				continue;
			}
			

			
			String daumHref=movieDoc.select("a.area_poster").get(0).attr("href");
			String daumCode=daumHref.substring(daumHref.lastIndexOf("=")+1,daumHref.lastIndexOf("#"));
			

			mvRank[flag][11] = daumCode;
			finalCnt++;
		
			}
	}
		return mvRank;
}
}
