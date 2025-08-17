import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from 'src/app/shared/post.service';
import { throwError } from 'rxjs';
import { CreatePostPayload } from './create-post.payload';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {

  name: string;
  createPostForm: FormGroup;
  postPayload: CreatePostPayload;

  constructor(private router: Router, private postService: PostService,
    private activatedRoute: ActivatedRoute, authService: AuthService,private toastr: ToastrService) {
    this.name = authService.getUserName();
    this.postPayload = {
      tweetName: '',
      description: '',
    }
  }

  ngOnInit() {
    this.createPostForm = new FormGroup({
      tweetName: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
    });
  }

  createPost() {
    this.postPayload.tweetName = this.createPostForm.get('tweetName').value;
    this.postPayload.description = this.createPostForm.get('description').value;
  
    this.postService.createPost(this.name,this.postPayload).subscribe((data) => {
      this.router.navigateByUrl('');
    }, error => {
      throwError(error);
    })
  }

  discardPost() {
    this.router.navigateByUrl('/');
  }

}