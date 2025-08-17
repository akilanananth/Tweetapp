import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { AuthService } from '../auth/shared/auth.service';
import { SignupRequestPayload } from '../auth/signup/singup-request.payload';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: SignupRequestPayload[];
  
  constructor(private authService: AuthService,private router: Router) { }

  ngOnInit(): void {
    this.authService.allUsers().subscribe((data) => {
      this.users = data;
      this.router.navigateByUrl('/users');
    }, error => {
      throwError(error);
    })
  }

}
