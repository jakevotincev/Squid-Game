import React, { Component } from 'react';
import {Client} from "@stomp/stompjs";

class Participant extends Component{
    state = {
        nickname: "",
        content: "",
        playerId: null
}
    handleChange = (event) => {
        // 👇 Get input value from "event"
        this.setState({nickname : event.target.value});


    }
    handleChange2 = (event) => {
        // 👇 Get input value from "event"
        this.setState({content : event.target.value});


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
        let url = "http://localhost:8080/account/";
        url = url.concat(this.state.nickname)
        fetch(url,{
            headers: {
                // "Content-Type": "application/json"
            },
            method: 'GET',
            mode: 'cors'
        }).then(
            res => {res.json().then(data =>{

                 this.setState({playerId: data.id})
                console.log(JSON.stringify(data))
            })
            }

        )
        console.log(this.state.playerId);
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
            this.client.subscribe('/glavniy/messages', message => {
                console.log(JSON.parse(message.body));
                });
        } else {
            // Queue a retry
            setTimeout(() => { this.handleSend() }, 100)
        }
    };
    clickHandler = () => {
        console.log(this.state.nickname);
        console.log(this.state.content);
        console.log(this.state.playerId);
        const anketa ={
            playerId: this.state.playerId,
            content: this.state.content
        }
        // this.client.publish({destination: '/app/sendCriteria', body: JSON.stringify(anketa) });
        fetch('http://localhost:8080/savePlayerForm', {  // Enter your IP address here
            headers: {
                "Content-Type": "application/json"
            },
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(anketa) // body data type must match "Content-Type" header

        })
    }
    render(){return(
        <div id="participant_page" align="center">
            <button id="connect" className="btn btn-default" type="submit" onClick={this.componentDidMount}>Connect</button>
            <h1>This is participant page</h1>
            <div id="anketa" align="center">
                <h3>Пожалуйте заполните анекту участника</h3>
                <label>Никнейм: </label>
                <input type="text" id="nick" name="nick" placeholder="Введите ваш никнейм" onChange={this.handleChange}/>
                <br/>
                <label>Ваша анкета: </label>
                <input type="text" id="anketa" name="anketa" placeholder="Введите текст вашей анкеты участника " height="400" width="200" onChange={this.handleChange2}/>
                <br/><br/>
                <button type="submit" onClick={this.clickHandler}>Отправить</button>
            </div>
        </div>
    );
    }
}
export default Participant;