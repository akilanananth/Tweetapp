import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { throwError } from 'rxjs';
import { LoginRequestPayload } from '../login/login-request.payload';
import { AuthService } from '../shared/auth.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  
  loginForm: FormGroup;
  loginRequestPayload: LoginRequestPayload;
  
  constructor(private authService: AuthService, private activatedRoute: ActivatedRoute,
    private router: Router) {
      this.loginRequestPayload = {
        username: '',
        password: ''
      };
     }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });
  }
  
  reset() {
    this.loginRequestPayload.username = this.loginForm.get('username').value;
    this.loginRequestPayload.password = this.loginForm.get('password').value;

    this.authService.resetPassword(this.loginRequestPayload).subscribe(data => {
      this.router.navigateByUrl('');
    }, error => {
      throwError(error);
    });
  }
}
