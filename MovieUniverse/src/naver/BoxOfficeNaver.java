package naver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BoxOfficeNaver {
	
	String url = "https://movie.naver.com/movie/running/current.nhn";	
	String title ="";	   // 영화 제목
	String score ="";	   // 영화 평점
	String bookRate ="";   // 영화 예매율
	String type ="";	   // 영화 장르
	String movieTime ="";  // 영화 상영시간
	String openDt ="";	   // 영화 개봉일
	String director ="";   // 영화 감독
	String actor ="";	   // 영화 출연진
	String naverCode = ""; // 네이버 영화코드
	int finalCnt = 0;      // 수집을 멈추기위한 변수(1~10위까지 완료)

	public String[][] naverMovieRank(String[][] mvRank) throws IOException{
		
		Document doc = Jsoup.connect(url).get();
		Elements movieList = doc.select("div.lst_wrap > ul.lst_detail_t1 > li");
		

		for (Element movie : movieList) { 
			if(finalCnt == 10) {
				// 1~ 10위까지의 영화정보 수집 완료! 빠져나가세요~
				break;
			}
			// 네이버 영화 정보 크롤링
			title = movie.select("dt.tit > a").text(); // 영화제목
			
			int flag = 0; // flag 는 999가 초기화
			// 타이틀이 같으면 그 정보 수집하게 하고  상위 10개 수집하면 빠져 나가게 하기.
			for (int i = 0; i < mvRank.length; i++) {
				if(mvRank[i][1].contentEquals(title)) {
					// BoxOffice 1~10위 권내의 영화로 판별 크롤링 시작@
					flag = i; //포함이 되면 flag가 해당 index로 변하고 for문 빠져나가면 다시 0으로 초기화 ( 0~9의 값만 input)
					break;
				}//else 
					//continue Label1; // 1~10  위 권내 아니면 그냥 빠져나가기~ 원래는 바로 상위for문 빠져나가게 하는 건데.. 우리는 지금
							  // 그렇게 하면 안되니까 상위 상위의 for문으로 돌아가라는 label을 붙여준다.
							  // 근데 만약에 a영화가 1위에 없어서 빠져나가면.. 그 영화가 2~10위 내에 있을 수도 있음.. 그래서 else쓰면 안된다.
					
				// 1~10 위 권 외의 영화 -> 크롤링X
				// flag가 0~9 사이의 값이면 크롤링 시작.
				// 그 외의 값(위에서 초기화 시킨 999값)이면 크롤링 하지 말아라.
				if( flag < 0 || flag > 10) {
					continue; // 해당 영화가 1~10 위에 없다.. 그럼 다시 for문으로 가서 다른 영화 1~10위에 있나 보는 것.
				}
				
			}
			
			// 예매율, 감독, 출연진 초기화
			bookRate = "0"; 
			director = "";
			actor = "";
			



			if(movie.select("span.num").size() == 2) {
				bookRate = movie.select("span.num").get(1).text();
			} 
			
			score = movie.select("span.num").get(0).text(); // 평점
			type = movie.select("dd > span.link_txt").get(0).text(); // 장르
			
			
			String temp= movie.select("dl.info_txt1 > dd").get(0).text();
			int beginTimeIndex = temp.indexOf("|");
			int endTimeIndex = temp.lastIndexOf("|");

			
			if(beginTimeIndex == endTimeIndex) { // 상영시간
				movieTime = temp.substring(0, endTimeIndex);
			}else {
				movieTime =  temp.substring((beginTimeIndex+2), endTimeIndex);
			}
			
			// 0 : 없음, 1 : 있음
			int dCode = 0; // 감독 유무확인
			int aCode = 0; // 출연진 유무 확인
			if(!movie.select("dt.tit_t2").text().equals("")) {
				dCode = 1; // 감독있음!
			}if(!movie.select("dt.tit_t3").text().equals("")) {
				aCode = 1; // 출연진 있음!
			}if(dCode == 1 && aCode == 0) {
				director = movie.select("dd > span.link_txt").get(1).text(); // 감독이 있으면 무조건 1을 불러오게 만듦.
			}else if(dCode == 0 && aCode == 1) {
				actor = movie.select("dd > span.link_txt").get(1).text(); // 출연진
			} else if(dCode == 1 && aCode == 1) {
				director = movie.select("dd > span.link_txt").get(1).text(); // 감독
				actor = movie.select("dd > span.link_txt").get(2).text(); // 출연진
			}
			
			
			
			// 우리는 https://movie.naver.com/movie/bi/mi/basic.nhn?code=180378 에서 180378만 뽑을거임
			// python [0:5]    java (0,)
			String naverHref = movie.select("dt.tit > a").attr("href"); // 네이버 영화 URL
			naverCode = naverHref.substring(naverHref.lastIndexOf("=")+1); // naver 영화 code
			
			
			
			//영화 개봉일자
			int openDtTxtIndex = temp.lastIndexOf("개봉"); 
			openDt = temp.substring(endTimeIndex+2);
			
			// 수집된 영화정보를 mvRank에 input
			
			// 각각의 flag 값의 배열에 정보를 담는다.
			mvRank[flag][2] = bookRate;
			mvRank[flag][3] = type;
			mvRank[flag][4] = movieTime.trim();
			mvRank[flag][5] = openDt.trim();
			mvRank[flag][6] = director;
			mvRank[flag][7] = actor;
			mvRank[flag][10] = naverCode;
			finalCnt++;
			
		}
		
		return mvRank;	
		
	
		
		
	}
	

}
