import React, { Component } from 'react';
import {Client} from "@stomp/stompjs";
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';

class Worker extends Component{
    state = {
        nickname: ""
    }
    handleChange = (event) => {
        // 👇 Get input value from "event"
        this.setState({nickname : event.target.value});

    }

    getFromInputz(inputName){
        return document.getElementById(inputName).value;
    }
    getNickname() {
        let username = this.getFromInputz('nick');
        console.log(username);
        let url= 'ws://localhost:8080/squid-game-socket?username=';
        url = url.concat(this.state.nickname);
        let url2 = 'ws://localhost:8080/squid-game-socket?username=Грудыгло';
        return url
    }
    componentDidMount = () => {
        if (this.state.nickname !== ""){
            // setTimeout(() => { this.componentDidMount() }, 2000);
            console.log('Component did mount');
            console.log(this.state.nickname);
            // The compat mode syntax is totally different, converting to v5 syntax
            // Client is imported from '@stomp/stompjs'
            this.client = new Client();

            this.client.configure({
                brokerURL: this.getNickname(),
                onConnect: () => {
                    console.log('onConnect');
                    this.handleSend();
                },
                // Helps during debugging, remove in production
                debug: (str) => {
                    console.log(new Date(), str);
                }
            });

            this.client.activate();}

    }
    handleSend = () => {
        if (this.client.webSocket.readyState === WebSocket.OPEN) {
            this.client.subscribe('/user/worker/messages', message => {
                console.log(JSON.parse(message.body));
            });
        } else {
            // Queue a retry
            setTimeout(() => { this.handleSend() }, 100)
        }
    };

    render(){return(
        <div>
            <h2>THis is worker page</h2>
            <button id="connect" className="btn btn-default" type="submit" onClick={this.componentDidMount}>Connect</button>
            <h3>Пожалуйте введите свой никнейм </h3>
            <label>Никнейм: </label>
            <input type="text" id="nick" name="nick" placeholder="Введите ваш никнейм" onChange={this.handleChange}/>
            <br/>

        </div>
    );
}}
export default Worker;