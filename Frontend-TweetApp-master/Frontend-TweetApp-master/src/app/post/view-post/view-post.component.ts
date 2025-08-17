import { Component, OnInit } from '@angular/core';
import { PostService } from 'src/app/shared/post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PostModel } from 'src/app/shared/post-model';
import { throwError } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { CommentPayload } from 'src/app/comment/comment.payload';
import { CommentService } from 'src/app/comment/comment.service';
import { AuthService } from 'src/app/auth/shared/auth.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent implements OnInit {

  postId: string;
  post: PostModel;
  commentForm: FormGroup;
  name: string;
  comment: string;

  constructor(private postService: PostService, private activateRoute: ActivatedRoute,
    private commentService: CommentService, private router: Router, authService: AuthService) {
    this.postId = this.activateRoute.snapshot.params.id;
    this.name = authService.getUserName();

    this.commentForm = new FormGroup({
      text: new FormControl('', Validators.required)
    });
    // this.commentPayload = {
    //   // id: '',
    //   text: '',
    //   postId: this.postId
    // };
  }

  ngOnInit(): void {
    this.getPostById();
    //this.getCommentsForPost();
  }

  postComment() {
    // this.commentPayload.id = this.commentForm.get('text').value;
    this.comment = this.commentForm.get('text').value;
    this.commentService.postComment(this.name,this.postId,this.comment).subscribe(data => {
      this.commentForm.get('text').setValue('');
      this.router.navigateByUrl('/user-profile/' + this.name);
      //this.getPostById();
    }, error => {
      throwError(error);
    })
  }

  private getPostById() {
    this.postService.getPost(this.postId).subscribe(data => {
      this.post = data;
    }, error => {
      throwError(error);
    });
  }

  // private getCommentsForPost() {
  //   this.commentService.getAllCommentsForPost(this.postId).subscribe(data => {
  //     this.comments = data;
  //   }, error => {
  //     throwError(error);
  //   });
  // }

}
