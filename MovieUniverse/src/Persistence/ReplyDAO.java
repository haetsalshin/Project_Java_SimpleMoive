package Persistence;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import domain.ReplyDTO;

public class ReplyDAO {
	
	MongoClient client = MongoClients.create(); // create()에 host num들어가는데 우리는 지금 내껄로 하니까 따로 값을 안넣어도 된다.
	MongoDatabase db = client.getDatabase("local"); // 주소 찾아가는 것.
	MongoCollection<Document> collection = db.getCollection("movie"); // 주소 찾아가는 것.

	
	// 저장하기
	// 댓글 1건 등록
	 public void addReply(ReplyDTO rDto) { //저장할 내용을 매개변수로 지정.ㅣ
		 Document doc = new Document("MovieNm",rDto.getMovieNm())
				 .append("content", rDto.getContent())
				 .append("writer", rDto.getWriter())
				 .append("score", rDto.getScore())
				 .append("regdate", rDto.getRegDate());
		 
		 collection.insertOne(doc); // -> one : 한건 / insertmanay 도 있다.
		 
	 }
	 // 댓글 삭제(등록하려는 영화의 댓글이 존재할 때 해당 영화 댓글만 삭제)
	 public void deleteReply(String movieNm) {
		 
		collection.deleteMany(new Document("movieNm", movieNm)); // deleteMany , deleteone도 있다.
				  
	 }
	 
}
