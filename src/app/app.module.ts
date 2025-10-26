import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { MachinesComponent } from './pages/machines/machines.component';
import { ErrorsComponent } from './pages/errors/errors.component';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { UsersComponent } from './pages/users/users.component';
import { UserFormComponent } from './pages/user-form/user-form.component';
import {FormsModule} from "@angular/forms";
import {MachineFormComponent} from "./pages/machine-form/machine-form.component";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UsersComponent,
    UserFormComponent,
    MachinesComponent,
    MachineFormComponent,
    ErrorsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
