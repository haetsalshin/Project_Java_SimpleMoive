package daum;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Persistence.ReplyDAO;
import domain.ReplyDTO;

public class ReplyCrawlerDaum {
	
	int page = 1;
	int cnt = 0;
	int total = 0;
	String prePage = "";
	
	ReplyDAO rDao = new ReplyDAO();
	
	public HashMap<String, Integer> daumCrawler(String movieNm, String daumCode) throws IOException {
	
		
		while (true) {
			
			String url = "https://movie.daum.net/moviedb/"
					+ "grade?movieId="+daumCode+"&type=netizen&page="+page;
				
			
			Document doc = Jsoup.connect(url).get();
			Elements replyList= doc.select("div.review_info");

			if(replyList.size() == 0) {
				break;
			}
			
			String writer = "";
			int score = 0;
			String content = "";
			String date = "";
		

			Elements movie = doc.select("div.review_info");
			
			for(Element reple : replyList) {

				writer = reple.select("em.link_profile").get(0).text();
				score = Integer.parseInt(reple.select("em.emph_grade").get(0).text());
				content = reple.select("p.desc_review").get(0).text();
				//String dateTemp = reple.select("span.info_append").get(0).text();
				
				date = reple.select("span.info_append").get(0).text().substring(0,10);
				
				System.out.println("[DAUM] ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
				System.out.println("작성자 : "+ writer);
				System.out.println("평점 : "+ score);
				System.out.println("내용 : "+ content);
				System.out.println("작성일자 : "+ date);
				
				// MongoDB에 저장(댓글 1건)
				
				ReplyDTO rDto = new ReplyDTO(movieNm, content, writer, score, date);
				rDao.addReply(rDto);
				cnt ++;
				total += score;

			}
			
			page++;			

			
		}
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ DAUM ["+cnt+"]건의 댓글 수집 완료");
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("cnt", cnt);
		map.put("total", total);
		
		return map;
		
		
		
	}
	
	

}
