import React, { Component } from 'react';
import {Client} from "@stomp/stompjs";
import "./pagestyle.css";
import {useLocation} from "react-router-dom";
import Clicker from "./Clicker";

export function withRouter(Children) {
    return(props)=>{
        const location = useLocation()
        return <Children {...props} location = {location}/>
    }
}
class Soldier extends Component {

    state = {
        nickname: "",
        soldierId: null,
        preyId: null,
        preyName: null,
        score: null,
        killStatusMsg: '',
        showKillStatusMsg: false,
        showClicker: false,
        trainingScore: 0,
        bonusScore: 0
    }

    handleChange = (event) => {
        // 👇 Get input value from "event"
        this.setState({nickname : event.target.value});


    }
    // getFromInputz(inputName){
    //     return document.getElementById(inputName).value;
    // }
    getNickname() {
        // let username = this.getFromInputz('nick');
        // console.log(username);
        this.setState({nickname: this.props.location.state})
        let url= 'ws://localhost:8080/squid-game-socket?username=';
        url = url.concat(this.props.location.state);
        return url
    }
    componentDidMount = () => {
        // console.log(this.props.location.state)
        this.setState({nickname: this.props.location.state})
        // console.log(this.state.nickname)
        let url = "http://localhost:8080/account/";
        url = url.concat(this.props.location.state)
        fetch(url,{
            headers: {
                // "Content-Type": "application/json"

                'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
            },
            method: 'GET',
            mode: 'cors'
        }).then(
            res => {res.json().then(data =>{

                this.setState({soldierId: data.id})
                // console.log(JSON.stringify(data))
            })
            }

        )
        // console.log(this.state.soldierId);
        if (this.props.location.state !== ""){
            // setTimeout(() => { this.componentDidMount() }, 2000);
            // console.log('Component did mount');
            // console.log(this.state.nickname);
            // The compat mode syntax is totally different, converting to v5 syntax
            // Client is imported from '@stomp/stompjs'
            this.client = new Client();
            this.client.configure({
                brokerURL: this.getNickname(),
                connectHeaders: {
                    Authorization: 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                },
                onConnect: () => {
                    this.handleSend();
                },
                // Helps during debugging, remove in production
                debug: (str) => {
                    // console.log(new Date(), str);
                }
            });

            this.client.activate();}

    }
    handleSend = () => {
        if (this.client.webSocket.readyState === WebSocket.OPEN) {
            const headers = { Authorization: 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)}
            this.client.subscribe('/soldier/messages', message => {
                // console.log('govnomapping ',JSON.parse(message.body));
                let sad = JSON.parse(message.body)
                if (sad.type === 'START_TRAINING') {
                    this.setState({showClicker: true})

                }
                if (sad.type === 'TRAINING_COMPLETED') {
                    this.setState({showClicker: false})
                    if (this.state.trainingScore !== 0) {
                        console.log(this.state.trainingScore)
                        let url = 'http://localhost:8080/soldier/'
                        url = url.concat(this.state.soldierId)
                        url = url.concat('/score')
                        if (this.state.trainingScore === 15) {
                            const bonus = (this.state.trainingScore / 13) + this.state.trainingScore * 1.2
                            this.setState({trainingScore: bonus + this.state.trainingScore})
                        }
                        // this.setState({trainingScore: this.state.trainingScore + this.state.bonusScore})
                        const soldierScoreMsg = {
                            score: this.state.trainingScore
                        }
                        fetch(url,{
                            headers: {
                                "Content-Type": "application/json",

                                'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                            },
                            method: 'POST',
                            mode: 'cors',
                            body: JSON.stringify(soldierScoreMsg)
                        })
                    }
                }
                else {
                    this.setState({preyId: sad.playerId});
                    this.setState({preyName: sad.playerName});
                    this.setState({score: Math.floor(Math.random() * 100)});
                }
            }, headers)
            this.client.subscribe('/user/soldier/messages', message => {
                // console.log('private ',JSON.parse(message.body));
                let msg = JSON.parse((message.body))
                if (msg.type === 'PLAYER_KILLED_MESSAGE') {
                    this.setState({killStatusMsg: 'Вы убили игрока '+ this.state.preyName})
                    this.setState({showKillStatusMsg: true})
                }
                if (msg.type === 'MISS_MESSAGE') {
                    this.setState({killStatusMsg: 'Вы промахнулись'})
                    this.setState({showKillStatusMsg: true})
                }
            }, headers)

        } else {
            // Queue a retry
            setTimeout(() => { this.handleSend() }, 100)
        }
    };
    clickHandler =()=>{

        const soldierMsg =
             {
                playerId: this.state.preyId,
                playerName: this.state.preyName,
                score: this.state.score,
                soldierId: this.state.soldierId
            }

        fetch('http://localhost:8080/killPlayer', {  // Enter your IP address here
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
            },
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(soldierMsg)
            // body data type must match "Content-Type" header

        }).then(
            //todo: add something
        )
    }
    addPoint = () => {
        this.setState({trainingScore: this.state.trainingScore + 1})
    }

    render(){return(
        <div id="soldier_page" align="center">
            <div id="connect">
            
            <h1 id="page_title">This is soldier page</h1>
            {/*<button id="connect" className="btn btn-default" type="submit" onClick={this.componentDidMount}>Connect</button>*/}
            <br/>
            {/*<label class="participant">Никнейм: </label>*/}
            {/*<input type="text" id="nick" name="nick" placeholder="Введите ваш никнейм" onChange={this.handleChange}/>*/}
            </div>
            { this.state.showClicker === false &&
            <div>
            <div class="participant" id="preys">
                <p>
                    Имя жертвы: {this.state.preyName}
                </p>
                <p>
                    Ваш счет выстрела: <label id="shoot_score">{this.state.score}</label>
                </p>
                <button type="submit" onClick={this.clickHandler}>Сделать выстрел</button>
            </div>
            {this.state.showKillStatusMsg === true &&
            <div>
                <h4>{this.state.killStatusMsg}</h4>
            </div>
            }
            </div>}
            { this.state.showClicker === true &&
                <div>
                    <Clicker
                    points={this.state.trainingScore}
                    onClick={this.addPoint}
                    name={'Сделано выстрелов : '}/>
                </div>
            }
        </div>

    )}
}
export default withRouter(Soldier)