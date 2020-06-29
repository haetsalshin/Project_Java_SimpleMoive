package naver;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Persistence.ReplyDAO;
import domain.ReplyDTO;

public class ReplyCrawlerNaver {
	
	int cnt = 0;
	int page = 1;
	int total =0; // 총 평점구하기
	String prePage = "";
	
	ReplyDAO rDao = new ReplyDAO();
	
	// 두건의 데이터(score, total)을 returnㅇ,로 보내는 방법
	// void -> HashMap<Strinig, Integer>
	// HashMap : python 의 dict type임.<> 제네릭 : <Key, Value>방식 . 수문장 같은 역할.
	// but 기본 자료형을 쓸 수 없다. 기본자료형인 int형을 객체자료형으로 쓸 수 있도록 가능하게 하는 것이 Wrapper Class(객체자료형)
	
	public HashMap<String, Integer> naverCrawler (String movieNm, String naverCode) throws Exception {

		while(true) {
			
			String url = "https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code"
					+ "="+naverCode+"&type=after&isActualPointWriteExecute"
					+ "=false&isMileageSubscriptionAlready"
					+ "=false&isMileageSubscriptionReject=false&page="+page;
			
			// 원래 특정 영화의 네이버 영화코드인 부분을 naverCode로 변경해준다.
			// 그래야 사용자가 선택한 영화가 알맞는 코드로 들어가기 때문이다.
			
			// 페이지네이션 돌면서 1페이지부터 끝까지~ 영화내용, 평점, 작성자, 작성일자 수집
			
			Document doc = Jsoup.connect(url).get();
			
			String content="";
			int score = 0; // 나중에 데이터 모아서 평점만 따로 편집 할 수 있기 때문에 String type 보다는 int 타입이 낫다.
			String writer = "";
			String date = "";
			
			Elements movieList = doc.select("div.score_result > ul > li");
			String nowPage =doc.select("input#page").attr("value");
			//System.out.println(">>>>>>>>>>>" +prePage + "," +nowPage);
			
			if(nowPage.equals(prePage)) {
				break;
			}else {
				prePage= nowPage;
			}
			
			int index = 0;
			
			for (Element movie : movieList) { 
				
				content = movie.select("span#_filtered_ment_"+(index)).text();
				score = Integer.parseInt(movie.select("div.star_score > em").get(0).text()); // String type => int type 형변환 해줌.		
				writer = movie.select("div.score_reple > dl > dt em ").get(0).text();
				int writer_Cut= writer.indexOf("(");
				date =  movie.select("div.score_reple > dl > dt em ").get(1).text();
				int date_cut = date.lastIndexOf(" ");
				
				String regDate = date.substring(0, date_cut);
				
				System.out.println(" [NAVER] ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
				System.out.println("■■내용 : "+content);
				System.out.println("■■평점 : "+score+"점");
				
				
				if ( writer_Cut > 0) {
					System.out.println("■■작성자 :"+writer.substring(0, writer_Cut));
				}else {
					System.out.println("■■작성자 :"+writer);
				}
				System.out.println("■■작성일자 :"+ regDate);
				
				// MongoDB에 저장(댓글 1건)
				ReplyDTO rDto = new ReplyDTO(movieNm, content, writer, score, regDate);
				//System.out.println(rDto.toString()); // toString은 내꺼 가방에 값 들어 있는지 확인 하기 위하여 사용
				// 안그러면 System.out.println(MovieNm, content, writer, score, regDate); 이런식으로 내가 다 써줘야함; 갯수 많아지면 노답
				rDao.addReply(rDto); // rDto 에  내용 평점 작정자 작성일자 넣어야함
				
				total += score; // 총 평균 평점을 구해볼 것임. 그러기 위해선 평점을 모두 더한 값이 필요하다.
				index++;
				cnt++;
				
			}
			page ++;

		}
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ NAVER ["+cnt+"]건의 댓글 수집 완료");
		
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	map.put("cnt", cnt);
	map.put("total", (total));
	return map;
		
	}
	

}
