import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListComponent } from './components/list/list.component';
import { FormComponent } from './components/form/form.component';
import { DetailComponent } from './components/detail/detail.component';


const routes: Routes = [
  { title: 'Posts', path: '', component: ListComponent },
  { title: 'Posts - create', path: 'create', component: FormComponent },
  { title: 'Posts - detail', path: 'detail/:id', component: DetailComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PostRoutingModule { }
