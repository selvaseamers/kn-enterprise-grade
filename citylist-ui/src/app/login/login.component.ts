import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NonNullableFormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../user';
import { AuthService } from '../auth.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  authForm: FormGroup;
  isSubmitted = false;
  isAuthorized = true;

  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) {
    this.authForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.authForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  public get formControls() {
    return this.authForm.controls;
  }

  signIn() {
    this.isSubmitted = true;
    if (this.authForm.invalid) {
      return;
    }
    const userData = this.authForm.value
  
    this.authService.signIn(this.authForm.value).subscribe(
			(data) => {
				localStorage.setItem('ACCESS_TOKEN', btoa(userData.email + ':' + userData.password));
        localStorage.setItem('ROLE', data as string);
        this.router.navigateByUrl('/admin');
			},(error) => {
				console.error('error caught in component');
				console.log(error);
				if(error.status == 401){
					console.log('redirecting to login');
					this.isAuthorized = false
          this.authForm.reset()
				}
			})
  }
}
