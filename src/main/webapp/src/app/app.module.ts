import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ApiInterceptor } from "./core/interceptors/api-interceptor";
import { RouterModule } from '@angular/router';
import { MatSelectModule } from "@angular/material/select";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { MatOptionModule } from "@angular/material/core";
import { MatIconModule } from "@angular/material/icon";
import { MatListModule } from "@angular/material/list";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { PageNotFoundComponent } from "./pages/page-not-found/page-not-found.component";
import { AdminComponent } from './pages/admin/admin.component';

@NgModule({ declarations: [
        AppComponent,
        HomeComponent,
        LoginComponent,
        RegisterComponent,
        PageNotFoundComponent,
        AdminComponent,
    ],
    bootstrap: [AppComponent], imports: [BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatSelectModule,
        MatIconModule,
        MatListModule,
        MatOptionModule,
        MatCheckboxModule], providers: [{
            provide: HTTP_INTERCEPTORS,
            useClass: ApiInterceptor,
            multi: true
        }, provideHttpClient(withInterceptorsFromDi())] })
export class AppModule { }
