import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  private client!: Client;
  private machineUpdates$ = new Subject<any>();

  connect() {
    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
      debug: () => {}
    });

    this.client.onConnect = () => {
      this.client.subscribe('/topic/machines', (message: IMessage) => {
        const data = JSON.parse(message.body);
        this.machineUpdates$.next(data);
      });
    };

    this.client.activate();
  }

  onMachineUpdate() {
    return this.machineUpdates$.asObservable();
  }
}

