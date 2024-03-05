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
    kolvoUserov: 0,
    showResults: false,
    info: ''
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
        this.setState({info: 'Ожидание подключения игроков'})
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
        // console.log('message: ',JSON.parse(message.body));

        let sad = JSON.parse(message.body);
        if (sad.type === 'FORMS_SELECTION_COMPLETED'){
          this.setState({info: 'Все участники отправили анкеты'})
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
        if (sad.type === 'LUNCH_STARTED') {
          this.setState({info: 'Рабочие начали готовку еды для игроков'})
        }
        if (sad.type === 'FOOD_PREPARED') {
          this.setState({info: 'Игроки борются за получение блюд'})
        }
        if (sad.type === 'STARTED_ROUND_PREPARINGS') {
          this.setState({info: 'Рабочие начали подготовку игры'})
        }
        if (sad.type === 'ROUND_PREPARING_COMPLETED') {
          this.setState({info: 'Рабочие завершили подготовку игры'})
        }
        if (sad.type === 'START_TRAINING') {
          this.setState({info: 'Началась тренировка солдат'})
        }
        if (sad.type === 'TRAINING_COMPLETED') {
          this.setState({info: 'Cолдаты завершили тринировку'})
        }
        if (sad.type === 'START_CLEANING') {
          this.setState({info: 'Рабочие начали уборку'})
        }
        if (sad.type === 'STOP_CLEANING') {
          this.setState({info: 'Рабочие завершили уборку'})
        }
        if (sad.type === 'RESULTS_READY') {
          this.setState({info: 'Cолдаты завершили тринировку'})
          this.setState({showResults: true})
          fetch('http://localhost:8080/account/1/results', {
            headers: {
              "Content-Type": "application/json",
              'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
          }).then(res => {
            res.json().then(data => {
              let finulResultsArr = [{}]
              let playersResultArr = []
              let workersResultArr = []
              let soldiersResultArr = []
              finulResultsArr = data
              finulResultsArr.forEach(account => {
                if (account.role === 'PLAYER') {
                  playersResultArr.push(account)
                }
                if (account.role === 'WORKER') {
                  workersResultArr.push(account)
                }
                if (account.role === 'SOLDIER') {
                  soldiersResultArr.push(account)
                }
              })
              workersResultArr.sort((a, b) => a.score > b.score ? 1 : -1)
              soldiersResultArr.sort((a, b) => a.score > b.score ? 1 : -1)
              playersResultArr.sort(a => a?.participatesInGame ? 1 : -1).sort((a, b) => a.score > b.score ? 1 : -1)
              console.log(workersResultArr, 'worker')
              console.log(soldiersResultArr, 'soldier')
              console.log(playersResultArr, 'player')

              const resultsArea = document.getElementById('results')

              function createSoldierTable() {

                const tableSoldier = document.createElement('table')
                tableSoldier.style.padding = "15px"
                tableSoldier.style.border = "2px solid coral"
                tableSoldier.style.textAlign = "center"
                const headerRow = tableSoldier.insertRow(0)
                headerRow.innerText = 'Итоги солдат'
                const columnHeaderRow = tableSoldier.insertRow(1)
                columnHeaderRow.insertCell(0).innerText = 'id'
                columnHeaderRow.insertCell(1).innerText = 'имя'
                columnHeaderRow.insertCell(2).innerText = 'роль'
                // columnHeaderRow.insertCell(3).innerText = 'живой'
                columnHeaderRow.insertCell(3).innerText = 'счет'
                for (let i = 0; i < soldiersResultArr.length; i++) {
                  const row = tableSoldier.insertRow(i + 2)
                  row.insertCell(0).innerText = soldiersResultArr[i].id
                  row.insertCell(1).innerText = soldiersResultArr[i].username
                  row.insertCell(2).innerText = soldiersResultArr[i].role
                  // row.insertCell(3).innerText = soldiersResultArr[i]?.participatesInGame
                  row.insertCell(3).innerText = soldiersResultArr[i].score

                }

                return tableSoldier
              }

              function createWorkersTable() {
                const tableWorker = document.createElement('table')
                tableWorker.style.padding = "15px"
                tableWorker.style.border = "2px solid coral"
                tableWorker.style.textAlign = "center"
                // tableWorker.style={border: "5px", padding: '15px', textAlign: 'center' }
                const headerRow = tableWorker.insertRow(0)
                headerRow.innerText = 'Итоги рабочих'
                const columnHeaderRow = tableWorker.insertRow(1)
                columnHeaderRow.insertCell(0).innerText = 'id'
                columnHeaderRow.insertCell(1).innerText = 'имя'
                columnHeaderRow.insertCell(2).innerText = 'роль'
                // columnHeaderRow.insertCell(3).innerText = 'живой'
                columnHeaderRow.insertCell(3).innerText = 'счет'
                for (let i = 0; i < soldiersResultArr.length; i++) {
                  const row = tableWorker.insertRow(i + 2)
                  row.insertCell(0).innerText = workersResultArr[i].id
                  row.insertCell(1).innerText = workersResultArr[i].username
                  row.insertCell(2).innerText = workersResultArr[i].role
                  // row.insertCell(3).innerText = workersResultArr[i]?.participatesInGame
                  row.insertCell(3).innerText = workersResultArr[i].score

                }
                return tableWorker
              }

              function createPlayersTable() {
                const tablePlayer = document.createElement('table')
                tablePlayer.style.padding = "15px"
                tablePlayer.style.border = "2px solid coral"
                tablePlayer.style.textAlign = "center"

                // ={border: "5px", padding: '15px', textAlign: 'center' }
                const headerRow = tablePlayer.insertRow(0)
                headerRow.innerText = 'Итоги игроков'
                const columnHeaderRow = tablePlayer.insertRow(1)
                columnHeaderRow.insertCell(0).innerText = 'id'
                columnHeaderRow.insertCell(1).innerText = 'имя'
                columnHeaderRow.insertCell(2).innerText = 'роль'
                columnHeaderRow.insertCell(3).innerText = 'живой'
                columnHeaderRow.insertCell(4).innerText = 'счет'
                for (let i = 0; i < playersResultArr.length; i++) {
                  const row = tablePlayer.insertRow(i + 2)
                  row.insertCell(0).innerText = playersResultArr[i].id
                  row.insertCell(1).innerText = playersResultArr[i].username
                  row.insertCell(2).innerText = playersResultArr[i].role
                  if (playersResultArr[i]?.participatesInGame) {
                    row.insertCell(3).innerText = 'Выжил'
                  } else { row.insertCell(3).innerText = 'Мертв' }
                  row.insertCell(4).innerText = playersResultArr[i].score
                }
                return tablePlayer
              }

              resultsArea.appendChild(createSoldierTable())
              resultsArea.appendChild(createWorkersTable())
              resultsArea.appendChild(createPlayersTable())
            })
          })
          this.setState({info: 'Итоговая таблица результатов'})
        }
        else {
          this.setState({info: 'Получены критерии игры от манагера'})
          this.setState({showMapRolesBtn: false})
          this.setState({playersNumber: sad.criteria?.playersNumber});
          this.setState({criteria: sad.criteria?.criteria});
          // console.log('playersNumber :',sad.criteria.playersNumber);
          // console.log('criteria',sad.criteria.criteria);
          this.setState({criteriaMsg: 'Предложенное количество участников : ' + sad.criteria?.playersNumber + " Критерии отбора : " + sad.criteria?.criteria});
          this.setState({criteriaMsgIsReceived: true});
          // console.log('Criteria msg is recieved: ',this.state.criteriaMsgIsReceived);
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
    this.setState({info: 'Отбор анкет участников прерван'})
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
    this.setState({info: 'Подготовка раунда игры прервана'})
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
    this.setState({info: 'Отправлены результаты игры'})

  }


  startGame = () =>{
    fetch('http://localhost:8080/startGame',{
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer ' + localStorage.getItem('glavniy')
      },
      method: 'GET',
      mode: 'cors'
    }).then(
        //todo: добавить что нибудь
    )
    this.setState({info: 'Начался раунд игры'})
  }
  mapRole = () => {
    fetch('http://localhost:8080/startRolesDistribution',{
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('glavniy')
      },
      method: 'GET',
      mode: 'cors'
    })
    this.setState({info: 'Распределены роли участников'})
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
    this.setState({info: 'Критерии игры утверждены, начинается отбор игроков'})
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
    this.setState({info: 'Критерии игры не утверждены'})
  }

  render() {
    return (
        <div className="App">
          <header className="App-header">
              <div>
                <button onClick={this.mapRole}>Распределить роли и начать игру</button>
              </div>
            <div style={{margin: '7px'}}>
            {this.state.info}
            </div>
            <br/>
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
              <br/>
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
            {this.state.showResults === true &&
                <div id="results" style={{display: "flex", marginLeft: '300px', marginTop: '100px'}}>
                </div>}
          </header>
        </div>
    );
  }
}

export default Glavniy;