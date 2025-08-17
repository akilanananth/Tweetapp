import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { faThumbsUp } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { PostModel } from '../post-model';
import { PostService } from '../post.service';

@Component({
  selector: 'app-all-post-tile',
  templateUrl: './all-post-tile.component.html',
  styleUrls: ['./all-post-tile.component.css']
})
export class AllPostTileComponent implements OnInit {
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

  likePost(id: string): void {
    this.postService.likePost(this.name,id).subscribe(data => {
    });
  }

}
