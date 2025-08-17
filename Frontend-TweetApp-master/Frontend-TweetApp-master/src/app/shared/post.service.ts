import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PostModel } from './post-model';
import { Observable } from 'rxjs';
import { CreatePostPayload } from '../post/create-post/create-post.payload';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  
  constructor(private http: HttpClient) { }

  getAllPosts(): Observable<Array<PostModel>> {
    return this.http.get<Array<PostModel>>('http://localhost:8080/api/v1.0/tweets/all');
  }

  createPost(name: string,postPayload: CreatePostPayload): Observable<any> {
    return this.http.post('http://localhost:8080/api/v1.0/tweets/'+ name + '/add/', postPayload);
  }

  getPost(id: string): Observable<PostModel> {
    return this.http.get<PostModel>('http://localhost:8080/api/v1.0/tweets/tweet/' + id);
  }

  getAllPostsByUser(name: string): Observable<PostModel[]> {
    return this.http.get<PostModel[]>('http://localhost:8080/api/v1.0/tweets/' + name);
  }

  likePost(name: string,id: string): Observable<any>{
    return this.http.put('http://localhost:8080/api/v1.0/tweets/'+ name + '/like/'+ id,null); 
  }

  updatePost(name: string, id: string, postPayload: CreatePostPayload) {
    return this.http.put('http://localhost:8080/api/v1.0/tweets/'+ name + '/update/'+ id, postPayload);
  }

  deletePost(name: string, id: string) {
    return this.http.delete('http://localhost:8080/api/v1.0/tweets/'+ name + '/delete/'+ id); 
  }

}
