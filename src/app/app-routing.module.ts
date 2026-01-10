import { MachinesComponent } from './pages/machines/machines.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { UsersComponent } from './pages/users/users.component';
import { UserFormComponent } from './pages/user-form/user-form.component';
import { MachineFormComponent } from './pages/machine-form/machine-form.component';
import { AuthGuard } from './guards/auth.guard';
import { ErrorsComponent } from './pages/errors/errors.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'users', component: UsersComponent,canActivate: [AuthGuard] },
  { path: 'users/new', component: UserFormComponent,canActivate: [AuthGuard] },
  { path: 'users/edit/:id', component: UserFormComponent,canActivate: [AuthGuard] },

  { path: 'machines', component: MachinesComponent, canActivate: [AuthGuard] },
  { path: 'machines/new', component: MachineFormComponent, canActivate: [AuthGuard] },
  { path: 'errors', component: ErrorsComponent, canActivate: [AuthGuard] },


  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
  { path: '', component: MachinesComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
