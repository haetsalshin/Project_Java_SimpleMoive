package domain;

public class ReplyDTO {
	
	// DTO 만드는 순서
	// 1. 변수만들기(private반드시 붙여줘야한다)
	
	private String movieNm;   // 영화제목
	private String content;   // 댓글 내용
	private String writer;    // 댓글 작성자
	private double score;     // 댓글 평점. 소수점으로 나올 수도 있으니까 double로
	private String regDate;   // 댓글 작성일자
	

	// 2. 기본생성자 만들기
	public ReplyDTO() { // 디폴트 생성자. 아무것도 못담는 가방
	}

	// 변수들이 기본 생성자에 모드 들어간 생성자 만들기. 변수 5개를 담은 가방
	// 메서드 오버로딩.
	// 생성자 이름은 클래스 이름이랑 같게 한다.
	public ReplyDTO(String movieNm, String content, String writer, double score, String regDate) {
		super();
		this.movieNm = movieNm;
		this.content = content;
		this.writer = writer;
		this.score = score;
		this.regDate = regDate;
	}

	
	// 3. Getter Setter 만들기
	public String getMovieNm() {
		return movieNm;
	}

	public void setMovieNm(String movieNm) {
		this.movieNm = movieNm;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	// 4. toStriing() 만들기
	// toString은 내꺼 가방에 값 들어 있는지 확인 하기 위하여 사용
	@Override
	public String toString() {
		return "ReplyDTO [movieNm=" + movieNm + ", content=" + content + ", writer=" + writer + ", score=" + score
				+ ", regDate=" + regDate + "]";
	}
	

	
	
	

	
	
	
	
	
	
	
	
}
