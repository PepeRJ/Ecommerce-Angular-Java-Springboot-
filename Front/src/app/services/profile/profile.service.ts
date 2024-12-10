import { inject, Injectable} from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { Profile } from '../../interfaces/profile/profile';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';





@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private apiUrl = `${environment.backendUrl}/autenticacion/perfil`;
  private http= inject(HttpClient)
  constructor() {}

  obtenerDatosUsuario(idUsuario: number): Observable<Profile> {
    const url = `${this.apiUrl}/${idUsuario}`;
    const opciones = {
      withCredentials: true
    };

    return this.http.get<Profile>(url, opciones).pipe(
      catchError(error => {
        if (error.status === 401) {
        }
        return throwError(() => new Error(error.message || 'Error al obtener datos de usuario'));
      })
    );
  }

  editarPerfil(idUsuario: number, datosActualizados: any): Observable<any> {
    const url = `${this.apiUrl}/${idUsuario}`;
    const opciones = { 
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };

    console.log('URL:', url);
    console.log('Datos enviados:', datosActualizados);

    return this.http.put(url, datosActualizados, opciones).pipe(
      tap(response => console.log('Respuesta exitosa:', response)),
      catchError(error => {
        console.error('Error completo:', error);
        console.error('Error body:', error.error);
        console.error('Error status:', error.status);
        return throwError(() => new Error(`Error al editar perfil: ${error.error?.message || error.message}`));
      })
    );
  }

  obtenerPedidosUsuario(idUsuario: number): Observable<any> {
    const url = `${environment.backendUrl}/pedido/${idUsuario}`;
    const opciones = {
      withCredentials: true
    };
    return this.http.get<any>(url, opciones).pipe(
      catchError(error => {
        return throwError(() => new Error(error.message || 'Error al obtener pedidos del usuario'));
      })
    );
  }
}

