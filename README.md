# 👩‍💻Project_Java_SimpleMoive👩‍💻
JAVA기반의 한국 영화 박스피스 1~10위까지의 정보를 파싱 및 크롤링하고 MongoDB저장 후 사용자에게 정보를 전달해주는 콘솔프로그래밍


## :heavy_check_mark:Developer Environment
  
  - Language : [:coffee: Java 1.8](#getting-started)
  - IDE Tool : [:computer: Eclipse](#running-the-tests)
  - Package Manager : [🔗 : MavenRepository ](#deployment)
  - Using Package : [jsoup, json-sinmple, mongo-java-driver](#built-with)
  - Using WebDriver : [Githubm, SourceTree](#built-with) 
  - Pasing URL : [한국 영화진흥위원회](http://www.kobis.or.kr/kobisopenapi/homepg/main/main.do)
  - Crawling URL :  [네이버 영화](https://movie.naver.com/),
                    [다음 영화](http://ticket2.movie.daum.net/Movie/MovieRankList.aspx)
  
## :heavy_check_mark:Hyperlink & Description of Files
#### 1.src/common
  - [SimpleMovieMain](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/src/common/SimpleMovieMain.java) : 프로그램 시작하는 곳 + 콘솔 프로그래밍 view단!
  - [BoxOfficeParser](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/src/common/BoxOfficeParser.java) : 한국영화진흥위원회에서 일별 박스오피스 정보 수집(랭크, 영화제목, 누적관객수, 누적 매출액)

#### 2.src/naver
  - [BoxOfficeNaver](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/src/naver/BoxOfficeNaver.java): naver서 BoxOffice 1~10위 까지 영화 정보(제목, 상영일자, 감독, 출연진 등) 및 코드(네이버 고유 코드) 수집
  - [ReplyCrawlerNaver](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/src/naver/ReplyCrawlerNaver.java) : naver에서 해당 영화의 댓글, 평점, 작성자, 작성일자 수집해서 MongoDB에 저장.
#### 3.src/daum
  - [BoxOfficeDaum](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/src/daum/BoxOfficeDaum.java): daum에서 BoxOffice 1~10위 까지 영화 코드 (다음 고유 영화코드) 수집
  - [ReplyCrawlerDaum](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/src/daum/ReplyCrawlerDaum.java) : daum에서 해당 영화의 댓글, 평점, 작성자, 작성일자 수집해서 MongoDB에 저장.
#### 3.src/persistence
  - [ReplyDAO](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/src/Persistence/ReplyDAO.java) : 네이버, 다음에서 수집한 영화 댓글 저장 또는 삭제할 때 사용하는 DAO
#### 3.src/domain
  - [ReplyDTO](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/src/domain/ReplyDTO.java) : 네이버, 다음에서 수집한 영화 댓글 수집 후 MongoDB에 저장 할 때 사용하는 DTO
#### 3.pom.xml
  - [pom.mxl](https://github.com/haetsalshin/Project_Java_SimpleMoive/blob/master/MovieUniverse/pom.xml): Maven에서 bulid할 Libaray 설명하는 장소

## :speech_balloon:How to use?
  first, you should get a key form [한국영화진흥위원회](http://www.kobis.or.kr/kobisopenapi/homepg/main/main.do)
  
  1. BoxOfficeParser에서 발급받은 Key를 교체한다.
  2. ReplyDAO에서 MongoDB를 세팅한다.(connect, DB, Collection등)\
  3. 메인 프로그램을 실행한다.
  4. 1 ~ 10위 중 원하는 영화를 선택한다. → 1~10의 숫자를 입력
  5. Run the Program!
