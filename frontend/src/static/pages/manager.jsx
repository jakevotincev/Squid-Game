import React, { Component } from 'react';
import {useRef} from 'react';
import {Client} from "@stomp/stompjs";
import { useState } from 'react';
class Manager extends Component{


    state = {
        glavniyAnswer: null,
        crit: '',
        numb: '',
        slovo: ''
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
            this.client.subscribe('/manager/messages', message => {
                console.log(JSON.parse(message.body));
                this.setState({glavniyAnswer: message.body});
                const sad = JSON.parse(message.body);
                if (sad.confirm) {
                    this.state.slovo = 'Критерии утверждены';
                }else{
                    this.state.slovo = 'Переделывай, причина отказа : ' + sad.decline;
                }
            });


        } else {
            // Queue a retry
            setTimeout(() => { this.handleSend() }, 100)
        }
    };

    clickHandler = () => {
        console.log(this.state.crit);
        console.log(this.state.numb);
        const criteriaMsg ={
        criteria : {playersNumber: this.state.numb,
            criteria: this.state.crit,
            gameId: 1}
        }
        this.client.publish({destination: '/app/sendCriteria', body: JSON.stringify(criteriaMsg) });
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
          </div>
        <br/><br/><br/>
        <div>
            <p>
            Ваш босс ответил : {this.state.slovo}
            </p>
        </div>
      </div>

    );
}
}

export default Manager;

