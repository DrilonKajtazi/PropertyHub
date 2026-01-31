import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminAuthGuard } from "./core/auth-guards/admin-auth.guard";
import { HomeComponent } from "./pages/home/home.component";
import { LoginComponent } from "./pages/login/login.component";
import { RegisterComponent } from "./pages/register/register.component";
import { PageNotFoundComponent } from "./pages/page-not-found/page-not-found.component";
import { AdminComponent } from './pages/admin/admin.component';

const routes: Routes = [
  // regular
  { path: '', component: HomeComponent },
  // auth
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  // admin
  { path: 'admin', canActivate: [AdminAuthGuard], component: AdminComponent },
  // 404
  { path: 'page-not-found', component: PageNotFoundComponent },
  { path: '**', redirectTo: '/page-not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'enabled' // Restore scroll position
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
