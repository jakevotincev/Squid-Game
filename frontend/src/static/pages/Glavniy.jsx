// todo огда он нажал кнопка начать распределение ролей если 8 = коннектед
import React, { Component } from 'react';
import { Client } from '@stomp/stompjs';
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';
import "./pagestyle.css";

class Glavniy extends Component {
// todo fix before start game error
  state = {
    criteriaMsg: null,
    decline: '',
    connected: false,
    criteriaMsgIsReceived: false,
    playersNumber: null,
    criteria: null,
    allAnketasIsCollected: false,
    showInterruptionBtn: false,
    showMapRolesBtn: false,
    kolvoUserov: 0
  }
  handleChange = (event) => {
    // 👇 Get input value from "event"
    this.setState({decline : event.target.value});


  }
  componentDidMount = () => {
    console.log('Component did mount');
    // The compat mode syntax is totally different, converting to v5 syntax
    // Client is imported from '@stomp/stompjs'
    this.client = new Client();

    this.client.configure({
      brokerURL: 'ws://localhost:8080/squid-game-socket?username=glavniy',
      connectHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('glavniy')
      },
      onConnect: () => {
        console.log('onConnect');
        this.handleSend();
      },
      // Helps during debugging, remove in production
      debug: (str) => {
        console.log(new Date(), str);
      }
    });

    this.client.activate();


  }

  //todo: 1. проверить баг с началом игры


  handleSend = () => {

    if (this.client.webSocket.readyState === WebSocket.OPEN) {
      const headers = { Authorization: 'Bearer ' + localStorage.getItem('glavniy')}
      this.client.subscribe('/glavniy/messages', message => {
        console.log('message: ',JSON.parse(message.body));

        let sad = JSON.parse(message.body);
        if (sad.type === 'FORMS_SELECTION_COMPLETED'){
          this.setState({criteriaMsgIsReceived: false});
          this.setState({showInterruptionBtn: false});
          this.setState({allAnketasIsCollected: true});
        }
        if (sad.type === 'USERS_STATUS_MESSAGE') {
          this.setState({kolvoUserov: sad.connectedUsers.length})
          if (sad.connectedUsers.length >= 10) {
            this.setState({showMapRolesBtn: true})
            this.setState({kolvoUserov: sad.connectedUsers.length})
          }
        }
        else {
          this.setState({showMapRolesBtn: false})
          this.setState({playersNumber: sad.criteria?.playersNumber});
          this.setState({criteria: sad.criteria?.criteria});
          // console.log('playersNumber :',sad.criteria.playersNumber);
          // console.log('criteria',sad.criteria.criteria);
          this.setState({criteriaMsg: 'Предложенное количество участников : ' + sad.criteria?.playersNumber + " Критерии отбора : " + sad.criteria?.criteria});
          this.setState({criteriaMsgIsReceived: true});
          console.log('Criteria msg is recieved: ',this.state.criteriaMsgIsReceived);
        }
      }, headers);

    } else {
      // Queue a retry
      setTimeout(() => { this.handleSend() }, 100)
    }
  };
  interuptPlayersSelection = () =>{
    fetch('http://localhost:8080/interruptPlayersSelection',{
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer ' + localStorage.getItem('glavniy')
      },
      method: 'GET',
      mode: 'cors'
    }).then()
  }
  interuptRoundPreparing = () => {
    fetch('http://localhost:8080/interruptRoundPreparing',{
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer ' + localStorage.getItem('glavniy')
      },
      method: 'GET',
      mode: 'cors'
    })
  }
  showResults = () => {
    fetch('http://localhost:8080/showResults',{
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer ' + localStorage.getItem('glavniy')
      },
      method: 'GET',
      mode: 'cors'
    })

  }


  startGame = () =>{
    fetch('http://localhost:8080/startGame',{
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer ' + localStorage.getItem('glavniy')
      },
      method: 'GET',
      mode: 'cors'
    }).then()
  }
  mapRole = () => {
    fetch('http://localhost:8080/startRolesDistribution',{
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('glavniy')
      },
      method: 'GET',
      mode: 'cors'
    })

    this.setState({showMapRolesBtn: false})
  }
  clickHandler = () => {
    // const sad = JSON.parse(message.body);
    const confirmMessage ={
      confirm: true,
      criteria: {
        playersNumber: this.state.playersNumber,
        criteria: this.state.criteria,
        gameId: 1
      },
      declineReason: null
    }
    this.client.publish({destination: '/app/sendAnswer', headers: {Authorization: 'Bearer ' + localStorage.getItem('glavniy')}, body: JSON.stringify(confirmMessage)});
    this.setState({showInterruptionBtn: true});
  }
  clickHandler2 = () => {
    // const sad = JSON.parse(message.body);
    const confirmMessage ={
      confirm: false,
      criteria: {
        playersNumber: this.state.playersNumber,
        criteria: this.state.criteria,
        gameId: 1
      },
      declineReason: this.state.decline
    }
    this.client.publish({destination: '/app/sendAnswer', headers: {Authorization: 'Bearer ' + localStorage.getItem('glavniy')}, body: JSON.stringify(confirmMessage)});
  }

  render() {
    return (
        <div className="App">
          <header className="App-header">
            {this.state.showMapRolesBtn === true &&
            <div>
              <button onClick={this.mapRole}>Распределить роли и начать игру</button>
            </div>
            }
            <div>Кол-во законнекченных пользователей : {this.state.kolvoUserov}</div>

            {this.state.criteriaMsgIsReceived === true &&
                <div className="ManagerMessagee" id="managerMessagee" align="center">
                  <p id="manager_message">
                    Сообщение от менеджера : {this.state.criteriaMsg ? this.state.criteriaMsg : 'no data'}
                  </p><p>
                  Вы согласны с такими условиями старта игры?
                </p>
                  <p>
                    <button id="yesBtn"  onClick={this.clickHandler}>Yes</button>
                    <button id="noBtn" onClick={this.clickHandler2}>No</button>
                  </p>
                  <p>
                      <label>Причина отказа :</label>
                      <input type ="text" id="decline" name="decline" placeholder="Введите причину отказа" onChange={this.handleChange}/>
                  </p>
                </div>

            }
            {this.state.showInterruptionBtn === true &&
                <button type="submit" onClick={this.interuptPlayersSelection}>Прервать отбор анкет участников</button>
            }
            <br/><div>
              <button type="submit" onClick={this.interuptRoundPreparing}>Прервать подготовку раунда</button>
            </div>
            <div>
              <button type="submit" onClick={this.showResults}>Отобразить итоговую таблицу</button>
            </div>
            {this.state.allAnketasIsCollected === true &&
            <div id="Start_of_the_game" align="center">
              <p>
                <h2>Анкеты участников игры в кальмара отобраны</h2>
              </p>
              <button type="submit" onClick={this.startGame}>Начать игру</button>
            </div>
            }
          </header>
        </div>
    );
  }
}

export default Glavniy;