import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { PostService } from '../post.service';
import { PostModel } from '../post-model';
import { faThumbsUp } from '@fortawesome/free-solid-svg-icons';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/auth/shared/auth.service';

@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class PostTileComponent implements OnInit {

  faThumbsUp = faThumbsUp;
  @Input() posts: PostModel[];
  name: string; 

  constructor(private postService: PostService,private router: Router,authService: AuthService) {
    this.name = authService.getUserName();
   }

  ngOnInit(): void {
  }

  goToPost(id: string): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

  updatePost(id: string): void {
    this.router.navigateByUrl('/update-post/' + id);
  }

  deletePost(id: string): void {
    this.postService.deletePost(this.name,id).subscribe(data => {
      this.router.navigateByUrl('');
    });
  }

  likePost(id: string): void {
    this.postService.likePost(this.name,id).subscribe(data => {
      this.router.navigateByUrl('/user-profile/' + this.name);
    });
  }

}
