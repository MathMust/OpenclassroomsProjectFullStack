import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { UnauthGuard } from './guards/unauth.guard';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AuthGuard } from './guards/auth.guard';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [

  {
    path: '',
    component: HomeComponent,
    canActivate: [UnauthGuard]
  },

  {
    path: 'auth',
    component: MainLayoutComponent,
    canActivate: [UnauthGuard],
    loadChildren: () =>
      import('./features/auth/auth.module').then(m => m.AuthModule)
  },

  {
    path: 'posts',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    loadChildren: () => import('./features/post/posts.module').then(m => m.PostsModule)
  },

  {
    path: 'topics',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    loadChildren: () => import('./features/topic/topics.module').then(m => m.TopicsModule)
  },

  {
    path: 'me',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    loadChildren: () => import('./features/me/me.module').then(m => m.MeModule)
  },

  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }