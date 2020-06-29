package common;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

import Persistence.ReplyDAO;
import daum.BoxOfficeDaum;
import daum.ReplyCrawlerDaum;
import naver.BoxOfficeNaver;
import naver.ReplyCrawlerNaver;


public class SimpleMovieMain {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in); // Scanner : 사용자에게 키보드로값을 입력 받는 것
		BoxOfficeParser bParser = new BoxOfficeParser(); // 객체를 생성함과 동시에 parsing할 url을 만들기 위해 생성자 사용..   
		BoxOfficeNaver bon = new BoxOfficeNaver();
		BoxOfficeDaum don = new BoxOfficeDaum();
		ReplyCrawlerNaver nCrawler = new ReplyCrawlerNaver();
		ReplyCrawlerDaum dCrawler = new ReplyCrawlerDaum();
		ReplyDAO rDao = new ReplyDAO();
		
		// 순위, 영화제목, 예매율, 장르, 상영시간, 개봉일자, 감독, 
		// 출연진, 누적관객수, 누적 매출액, 네이버 코드, 다음코드
		String [][] mvRank = new String[10][12];
		
		// 1. 박스오피스 정보 + 네이버 영화 정보 + 담을 영화 정보 (1~10 위)
		
		// 1-1. BoxOffice Parsing :)
		mvRank = bParser.getParser(); // url안보내고 호출만 할 거임.		
		
		// 1-2. Naver BoxOffice Crawling :)
		mvRank = bon.naverMovieRank(mvRank); 
		// 위에 4개만 채운(랭크, 영화명, 누적관객수 등) mvRank를 가져와서 naver 6개의 정보 채우고 다시 mvRank로 보내준다.
		
		// 1-3. Daum BoxOffice Crawling :)
		mvRank = don.daumMovieRank(mvRank);
		
		// 2/ View 단 실행
		// userVal = 사용자가 입력한 영화번호(순위)
		int userVal = userInterface(mvRank);
		
		// 3. 사용자가 선택한 영화의 네이버, 다음 댓글 정보를 수집 및 분석
		// 우리가 뭘 가져와야 할지를 생각해서 매개변수를 만든다.
		// 이미 영화네이버 댓글 주소는 우리가 알고 있다.
		// 우리가 현재 알아야 할 것은 어떤! 영화의 댓글을 가져올 것인지 이기 때문에 네이버 코드가 필요하다		
		
		// 수집하는 댓글의 영화가  MongoDB에 저장되어 있는 영화라면
		// 해당 영화 기존 댓글 우선 삭제 후 새로운 댓글 저장
		rDao.deleteReply(mvRank[userVal-1][1]);
	
		// 3-2. Naver 댓글 수집 + MongoDB저장
		HashMap<String, Integer>nMap =nCrawler.naverCrawler(mvRank[userVal-1][1],mvRank[userVal-1][10]); 
		// userVal(유저가 선택한 영화) 근데 컴퓨터는 0부터 시작한다는 것을 생각해야한다!
		// 따라서 -1을 해준다 :3, 네이버 코드
		// 그리고 우리가 불러온 naverCrawer 에 첫번째에 MovieNm또한 불러왔기 때문에 같게 해줘야 한다.
		
		// 3-2. Daum 댓글 수집 + MongoDB저장
		HashMap<String, Integer>dMap =dCrawler.daumCrawler(mvRank[userVal-1][1],mvRank[userVal-1][11]); 
		
		// 4. 사용자에게 결과 출력
		double dTotal=(double)dMap.get("total");
		double avgDaum = dTotal/dMap.get(("cnt")); 
		double nTotal=(double)nMap.get("total");
		double avgNaver = nTotal/nMap.get(("cnt")); // 6.136363636363637 이렇게 나오기 때문에 반올림을 해준다
		DecimalFormat dropDot = new DecimalFormat(".#"); // 소수점 첫번째자리까지만 나와라.
		DecimalFormat threeDot = new DecimalFormat("###,###"); // 관객수와 매출액도 우리가 보기 편하게 1000단위로 ,로 끊어주기.
		BigInteger money = new BigInteger(mvRank[userVal-1][9]); // 원래 int형은 한계가 있기 때문에 biginteger를 사용해서
																 // 거의 무한대의 값을 사용 할 수 있다.
																 // 유의할 점은 new BigInteger()안에는 String형만 넣을 수 있다~!
		

		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ Description of \"" + mvRank[userVal-1][1] + "\""); // \"" 치면 따옴표 출력 하라는 것! 문자열로 인식하지 말고!
		System.out.println("■■ 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("■■ 장르 : [" + mvRank[userVal-1][3] + "], 예매율 : [" + mvRank[userVal-1][2]+"%]");
		System.out.println("■■ 상영시간 : [" + mvRank[userVal-1][4] + "], 개봉일자 : [" + mvRank[userVal-1][5]+"]");
		System.out.println("■■ 감독 : [" + mvRank[userVal-1][6]+"]");
		System.out.println("■■ 출연진 : [" + mvRank[userVal-1][7]+"]");
		System.out.println("■■ 누적 ▶ [관객수 : " + threeDot.format(Integer.parseInt(mvRank[userVal-1][8])) + "명] [매출액 :" +
													threeDot.format(money)+"원]"); //관객수가 int형을 넘기 때문에 BigInteger사용
		System.out.println("■■ 네이버 ▶ [댓글수 : " + nMap.get("cnt")+ "] [평점 :" + dropDot.format(avgNaver)+"]");
		System.out.println("■■ 다음 ▶ [댓글수 : " + dMap.get("cnt")+ "] [평점 :" + dropDot.format(avgDaum)+"]");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		
		//printArr(mvRank); // 출력하는 코드만 따로 빼놓고 내가 그걸 출력하고 싶을 때는 명령어를 써주면 된다.
		
		
	}
	
	// 유저가 직접 값을 입력하는 부분을 메서드로 따로 만든다...
	// VIEW : 프로그램 시작인터페이스 + 사용자 값 입력
	public static int userInterface(String [][] mvRank) {
		Scanner sc = new Scanner(System.in); // Scanner : 사용자에게 키보드로값을 입력 받는 것
		int userVal = 0;

	// 2. View단
		
		// 2-1. 유저에게 BoxOffice 예매를 1~10위까지의 정보 제공
		
		// 현재 날짜 계산하기
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String today = sdf.format(cal.getTime());
		cal.get(Calendar.DAY_OF_MONTH);
		
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ SimpleMovie Ver1.2");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ >> Developer : Haetsal Shin(Sonne)");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ >> Today : "+ today);
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ >> RealTime based BoxOffice Rank : " +(cal.get(Calendar.MONTH)+1)+"월"+ cal.get(Calendar.DATE)+"일");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		

		for (int i = 0; i < mvRank.length; i++) {
			String noneCode = "";
			if(mvRank[i][10] == null) {
				noneCode = " >>>>>>>>>>>>>> 상영 정보 없음! Don't select this Movie";
			}
			
			System.out.println("■■ >> " + mvRank[i][0]+ "위 : " + mvRank[i][1] + noneCode);
		}

		// 유효성 체크
		// >> 1~10까지의 값
		// 1. 1~10 이외의 숫자를 넣었을 때
		
		// 1.2 사용자가 입력하는 부분
		while (true) {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ >> 보고싶은 영화 번호(순위)를 입력하세요.");
		System.out.print("■■ >> 번호 : ");
		//String userVal = sc.nextLine();// 콘솔창에 직접 입력하게 만들어준다.nextLine() : string type
		userVal = sc.nextInt();// 콘솔창에 직접 입력하게 만들어준다.nextInt() : int type
		
		//System.out.println(">> 사용자가 입력한 값은? " + userVal ); 
		
			if(userVal < 0 || userVal >10){
				// 잘못된 값 !
				// 원래는 0도 포함되면 안되지만 우리는 이스터 에그 값으로 만들꺼임><
				System.out.println("■■ >> [ Warning ] 1~10사이의 숫자를 입력하세요:(");
				continue;
				
				
		// 2. 정보없는 영화를 선택했을 때
			}else if(mvRank[userVal-1][10] == null) { 
				// 사용자가 입력한 번호의 영화가 정보가 있는지 없는지 체크

				System.out.println("■■ >> [ Warning ] 해당 정보는 상영정보가 없습니다. 다른 번호를 선택해주세요:(");
				continue;
			}else {
				// 사용자의 값이 0~10
				sc.close(); // sc로 전달받은 값의 행위가 끝나면 close
				break;
			}
		}
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
	
	return userVal; // 
	
}


public static void printArr(String[][] mvRank) {
	
	// mvRank 출력하는 코드
	System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
	System.out.printf("순위\t 영화제목\t예매율\t장르\t상영시간\t개봉일자\t감독\t 출연진\t누적관객수\t누적 매출액\t네이버 코드\t다음코드\n");
	System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	for ( int i = 0; i < mvRank.length; i ++) {
		
		System.out.print(mvRank[i][0] + "\t");
		System.out.print(mvRank[i][1] + "\t");
		System.out.print(mvRank[i][2] + "\t");
		System.out.print(mvRank[i][3] + "\t");
		System.out.print(mvRank[i][4] + "\t");
		System.out.print(mvRank[i][5] + "\t");
		System.out.print(mvRank[i][6] + "\t");
		System.out.print(mvRank[i][7] + "\t");
		System.out.print(mvRank[i][8] + "\t");
		System.out.print(mvRank[i][9] + "\t");
		System.out.print(mvRank[i][10] + "\t");
		System.out.println(mvRank[i][11]);
		
	}
	System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");		
	}

}
