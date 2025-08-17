import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { throwError } from 'rxjs';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';
import { CreatePostPayload } from '../create-post/create-post.payload';

@Component({
  selector: 'app-update-post',
  templateUrl: './update-post.component.html',
  styleUrls: ['./update-post.component.css']
})
export class UpdatePostComponent implements OnInit {
  postId: string;
  post: PostModel;
  postPayload: CreatePostPayload;
  updatePostForm: FormGroup;
  name: string;

  constructor(private postService: PostService, private activateRoute: ActivatedRoute,
    private router: Router, authService: AuthService) {
      this.postId = this.activateRoute.snapshot.params.id;
    this.name = authService.getUserName();
    this.postPayload = {
      tweetName: '',
      description: '',
    }
  }

  ngOnInit(): void {
    this.getPostById();
    this.updatePostForm = new FormGroup({
      tweetName: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
    });
  } 

  private getPostById() {
    this.postService.getPost(this.postId).subscribe(data => {
      this.post = data;
    }, error => {
      throwError(error);
    });
  }

  updatePost() {
    this.postPayload.tweetName = this.updatePostForm.get('tweetName').value;
    this.postPayload.description = this.updatePostForm.get('description').value;
    this.postService.updatePost(this.name,this.postId,this.postPayload).subscribe((data) => {
      this.router.navigateByUrl('/user-profile/' + this.name);
    }, error => {
      throwError(error);
    })
  }

  discardPost() {
    this.router.navigateByUrl('/');
  } 

}
