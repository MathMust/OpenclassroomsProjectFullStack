import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  constructor(private router: Router, public sessionService: SessionService) { }

  ngOnInit(): void { }

  goLogin() {
    this.router.navigate(['/auth/login']);
  }

  goRegister() {
    this.router.navigate(['/auth/register']);
  }

  logout() {
    this.sessionService.logOut();
    this.router.navigate(['/']);
  }
}
