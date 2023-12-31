
import React, { Component } from 'react';
import { Client } from '@stomp/stompjs';


class Glavniy extends Component {

  state = {
    criteriaMsg: null,
    decline: ''
  }
  handleChange = (event) => {
    // 👇 Get input value from "event"
    this.setState({decline : event.target.value});


  }
  componentDidMount() {
    console.log('Component did mount');
    // The compat mode syntax is totally different, converting to v5 syntax
    // Client is imported from '@stomp/stompjs'
    this.client = new Client();

    this.client.configure({
      brokerURL: 'ws://localhost:8080/squid-game-socket',
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



  handleSend = () => {
    if (this.client.webSocket.readyState === WebSocket.OPEN) {
      this.client.subscribe('/glavniy/messages', message => {
        console.log(JSON.parse(message.body));

        const sad = JSON.parse(message.body);
        console.log(sad.criteria.playersNumber);
        console.log(sad.criteria.criteria);
        this.setState({criteriaMsg: 'Предложенное количество участников : ' + sad.criteria.playersNumber + " Критерии отбора : " + sad.criteria.criteria});
      });

      // this.client.subscribe('/topic/manager/messages', message => {
      //   alert(message.body);
      // });
    } else {
      // Queue a retry
      setTimeout(() => { this.handleSend() }, 100)
    }
  };

  clickHandler = () => {
    // const sad = JSON.parse(message.body);
    const confirmMessage ={
      confirm: true,
      criteria: {
        playersNumber: 5,
        criteria: 'ref',
        gameId: 1
      },
      declineReason: null
    }
    this.client.publish({destination: '/app/sendAnswer', body: JSON.stringify(confirmMessage)});
  }
  clickHandler2 = () => {
    // const sad = JSON.parse(message.body);
    const confirmMessage ={
      confirm: false,
      criteria: {
        playersNumber: 5,
        criteria: 'ref',
        gameId: 1
      },
      declineReason: this.state.decline
    }
    this.client.publish({destination: '/app/sendAnswer', body: JSON.stringify(confirmMessage)});
  }

  render() {
    return (
        <div className="App">
          <header className="App-header">

            <p>
              Edit <code>src/App.js</code> and save to reload.
            </p>
            <div className="ManagerMessage" id="managerMessage">
            <p>
              Сообщение от менеджера : {this.state.criteriaMsg ? this.state.criteriaMsg : 'no data'}
              </p><p>
              Вы согласны с такими условиями старта игры?
            </p>
            <p>
              <button id="yesBtn"  onClick={this.clickHandler}>Yes</button>
              <button id="noBtn" onClick={this.clickHandler2}>No</button>
            </p>
            <p>
              <form>
                <label>Причина отказа :</label>
                <input type ="text" id="decline" name="decline" placeholder="Введите причину отказа" onChange={this.handleChange}/>
              </form>
            </p>
            </div>
            <a
                className="App-link"
                href="https://reactjs.org"
                target="_blank"
                rel="noopener noreferrer"
            >
              Learn React
            </a>
          </header>
        </div>
    );
  }
}

export default Glavniy;