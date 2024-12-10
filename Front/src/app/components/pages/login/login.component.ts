import { Component, inject } from '@angular/core';
import { LoginService } from '../../../services/login/login.service';
import { Router, RouterLink } from '@angular/router';
import { DatosRegistro, RespuestaLogin, Login } from '../../../interfaces/login/login';
import {  FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, FormsModule, ReactiveFormsModule, NgFor, NgIf, MatInputModule,  MatButtonModule, MatCardModule, MatFormFieldModule, MatIconModule, MatError],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm!: FormGroup;
  registroForm!: FormGroup;
  errorInicioSesion: string = '';
  errorRegistro: string = '';

  mostrarLogin: boolean = false;
  mostrarRegistro: boolean = false;
  mostrarCerrar: boolean = false;

  private fb = inject(FormBuilder);
  private loginSvc = inject(LoginService);
  private router = inject(Router);

  ngOnInit(): void {
    // Formulario de login actualizado con los nombres correctos
    this.loginForm = this.fb.group({
      correo_electronico: ['', [Validators.required, Validators.email]],
      contrasenya: ['', Validators.required]
    });

    // Formulario de registro
    this.registroForm = this.fb.group({
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      correo_electronico: ['', [Validators.required, Validators.email]],
      contrasenya: ['', Validators.required],
      direccion: ['', Validators.required],
      numero: ['', Validators.required]
    });
  }

  registro(): void {
    if (this.registroForm.invalid) {
      this.errorRegistro = 'Todos los campos son obligatorios';
      setTimeout(() => {
        this.errorRegistro = '';
      }, 5000);
      return;
    }

    const nuevoUsuario: DatosRegistro = {
      nombre: this.registroForm.get('nombre')?.value,
      apellidos: this.registroForm.get('apellidos')?.value,
      correo_electronico: this.registroForm.get('correo_electronico')?.value,
      direccion: this.registroForm.get('direccion')?.value,
      contrasenya: this.registroForm.get('contrasenya')?.value,
      numero: [this.registroForm.get('numero')?.value]
    };

    this.loginSvc.registro(nuevoUsuario).subscribe({
      next: (response) => {
        const idUsuario = response.usuario.id;
        this.router.navigate(['/perfil', idUsuario]);
        this.errorRegistro = '';
        this.registroForm.reset();
      },
      error: (error: any) => {
        this.manejarErrorRegistro(error);
      }
    });
  }

  manejarErrorRegistro(error: any): void {
    if (error.error && error.error.error === 'El usuario ya existe') {
      this.errorRegistro = 'Este correo electrónico ya está registrado.';
    } else {
      this.errorRegistro = (error.error && error.error.message) ? error.error.message : 'Este correo electrónico ya está registrado.';
    }
    setTimeout(() => {
      this.errorRegistro = '';
    }, 5000);
  }

  inicioSesion(): void {
    if (this.loginForm.invalid) {
      this.errorInicioSesion = 'Por favor, completa todos los campos.';
      return;
    }

    const credenciales: Login = {
      correo_electronico: this.loginForm.get('correo_electronico')?.value,
      contrasenya: this.loginForm.get('contrasenya')?.value
    };

    console.log('Enviando credenciales:', credenciales); // Para debugging

    this.loginSvc.iniciarSesion(credenciales).subscribe({
      next: (response: RespuestaLogin) => {
        const idUsuario = response.usuario.id;
        this.router.navigate(['/perfil', idUsuario]);
        this.loginForm.reset();
      },
      error: (error) => {
        this.errorInicioSesion = 'Error al iniciar sesión, inténtelo de nuevo.';
        setTimeout(() => {
          this.errorInicioSesion = '';
        }, 5000);
      }
    });
  }


  mostrarLoginForm(): void {
    this.mostrarLogin = true;
    this.mostrarRegistro = false;
    this.mostrarCerrar = true;
  }

  mostrarRegistroForm(): void {
    this.mostrarRegistro = true;
    this.mostrarLogin = false;
    this.mostrarCerrar = true;
  }

  cerrarFormularios(): void {
    this.mostrarLogin = false;
    this.mostrarRegistro = false;
    this.mostrarCerrar = false;
  }}
