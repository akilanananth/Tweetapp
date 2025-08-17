import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { AuthService } from '../shared/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  name: string;
  map: Map<string,string>;
  constructor(private authService: AuthService, private router: Router) {
    this.name = authService.getUserName();
   }

  ngOnInit(): void {
    this.authService.forgotPassword(this.name).subscribe((data) => {
      this.map = data;
      this.router.navigateByUrl('/forgot-password/'+ this.name);
    }, error => {
      throwError(error);
    })

  }

}
