// todo
import React, { Component } from 'react';
import {useRef} from 'react';
import {Client} from "@stomp/stompjs";
import { useState } from 'react';
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';
import "./pagestyle.css";
class Manager extends Component{


    state = {
        glavniyAnswer: null,
        crit: '',
        numb: '',
        slovo: '',
        allAnketasIsCollected: false,
        answerStatus: null
    }


        handleChange = (event) => {
            // 👇 Get input value from "event"
            this.setState({crit : event.target.value});


        }
    handleChange2 = (event) => {
        // 👇 Get input value from "event"
        this.setState({numb : event.target.value});

    }

    componentDidMount() {
        console.log('Component did mount');
        // The compat mode syntax is totally different, converting to v5 syntax
        // Client is imported from '@stomp/stompjs'
        this.client = new Client();

        this.client.configure({
            brokerURL: 'ws://localhost:8080/squid-game-socket?username=manager',
            connectHeaders: {
                Authorization: 'Bearer ' + localStorage.getItem('manager')
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
    handleSend = () => {
        if (this.client.webSocket.readyState === WebSocket.OPEN) {
            const headers = { Authorization: 'Bearer ' + localStorage.getItem('manager')}
            this.client.subscribe('/manager/messages', message => {

                console.log(JSON.parse(message.body));
                this.setState({glavniyAnswer: message.body});
                let sad = JSON.parse(message.body);
                if (sad.type === "ALL_FORMS_COLLECTED"){
                    this.setState({allAnketasIsCollected: true});
                }
                if (sad.type === 'FORMS_SELECTION_COMPLETED') {
                    fetch('http://localhost:8080/startLunch',{
                        headers: {
                            // "Content-Type": "application/json",
                            'Authorization': 'Bearer ' + localStorage.getItem('manager')
                        },
                        method: 'GET',
                        mode: 'cors'
                    })
                }
                this.setState({answerStatus: sad.confirm});
                // console.log(this.state.answerStatus);
                if (sad.confirm) {
                    this.state.slovo = 'Критерии утверждены';
                }else{
                    this.state.slovo = 'Переделывай, причина отказа : ';
                    this.state.slovo=  this.state.slovo.concat(sad.declineReason);
                }

            }, headers);
            this.client.subscribe('/user/worker/messages', message => {
                console.log(JSON.parse(message.body));
            }, headers)

        } else {
            // Queue a retry
            setTimeout(() => { this.handleSend() }, 100)
        }
    };

    clickHandler = () => {
        console.log(this.state.crit);
        console.log(this.state.numb);
        const criteriaMsg ={
        criteria : {
            playersNumber: this.state.numb,
            criteria: this.state.crit,
            gameId: 1}
        }

        this.client.publish({destination: '/app/sendCriteriaToGlavniy', headers: { Authorization: 'Bearer ' + localStorage.getItem('glavniy')}, body: JSON.stringify(criteriaMsg) });

    }
    sendAnketasHandler = () => {
        fetch('http://localhost:8080/sendCriteriaAndFormsToWorkers',{
            headers: {
                // "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        }).then(() => alert("Все пиздато"))

    }
    startRoundPreparation = () => {
        fetch('http://localhost:8080/startPrepareRound',{
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
}
    startTraining = () => {
        fetch('http://localhost:8080/startTraining',{
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }
    stopTraining = () => {
        fetch('http://localhost:8080/stopTraining',{
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }
    startCleaning = () => {
        fetch('http://localhost:8080/startCleaning',{
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }
    stopCleaning = () => {
        fetch('http://localhost:8080/stopCleaning',{
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }


    render() {return(

    <div id="manager_page">
          <h1>This is manager page</h1>
          <div className="Criteria_Message" id="criteria_message">
              <h3>Критерии</h3>
              <br />
              <form >
                  <label>Критерии отбора: </label>
                  <input type="text" id="crit" name="crit" placeholder="Введите количество участников" onChange={this.handleChange} />

              <br/>

                  <label>Количество участников: </label>
                  <input type="text" id="numb" name="numb" placeholder="Введите кол-во участников" onChange={this.handleChange2}/>
              </form>
              <br/>
              <button type="submit"  onClick={this.clickHandler}>Отправить</button>
              <div class="bossmsg">
            <Popup trigger={<button> Сообщение от босса </button>}
                   position="right centre">
            <p>
            Ваш босс ответил : {this.state.slovo}
            </p>
            </Popup>
                </div>
          </div>
        <br/><br/><br/>
        
        {this.state.allAnketasIsCollected === true &&
            // !(this.state.answerStatus) &&
            <div>
                <button type="submit" onClick={this.sendAnketasHandler}>Отправить анкеты рабочим</button>
            </div>

        }
        <div>
            <button type="submit" onClick={this.startRoundPreparation}>Отдать приказ о старте подготовки раунда</button>
        </div>
        <br/>
        <div>
            <button type="submit" onClick={this.startTraining}>Отдать приказ о начале тренировок солдат</button>
        </div>
        <br/>
        <div>
            <button type="submit" onClick={this.stopTraining}>Отдать приказ о прекращении тренировок солдат</button>
        </div>
        <div>
            <button type="submit" onClick={this.startCleaning}>Отдать приказ о начале уборки рабочим</button>
        </div>
        <br/>
        <div>
            <button type="submit" onClick={this.stopCleaning}>Отдать приказ о прекращении уборки рабочих</button>
        </div>
      </div>

    );
}
}

export default Manager;

