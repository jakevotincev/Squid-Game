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
        let token = JSON.parse(localStorage.getItem(`userData`))?.token
        // console.log(this.props.location.state)
        this.setState({nickname: this.props.location.state})
        // console.log(this.state.nickname)
        let url = "http://localhost:8080/account/";
        url = url.concat(this.props.location.state)
        fetch(url,{
            headers: {
                // "Content-Type": "application/json"

                'Authorization': 'Bearer ' + token
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
                    Authorization: 'Bearer ' + token
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
        let token = JSON.parse(localStorage.getItem(`userData`))?.token
        if (this.client.webSocket.readyState === WebSocket.OPEN) {
            const headers = { Authorization: 'Bearer ' + token}
            this.client.subscribe('/soldier/messages', message => {
                // console.log('govnomapping ',JSON.parse(message.body));
                let sad = JSON.parse(message.body)
                if (sad.type === 'START_TRAINING') {
                    this.setState({showClicker: true})

                }
                if (sad.type === 'TRAINING_COMPLETED') {
                    this.setState({showClicker: false})
                    if (this.state.trainingScore !== 0) {
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

                                'Authorization': 'Bearer ' + token
                            },
                            method: 'POST',
                            mode: 'cors',
                            body: JSON.stringify(soldierScoreMsg)
                        })
                    }
                } if (sad.type === 'RESULTS_READY') {
                    this.setState({showResults: true})
                    fetch('http://localhost:8080/account/2/results', {
                        headers: {
                            "Content-Type": "application/json",
                            'Authorization': 'Bearer ' + token
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
                            workersResultArr.sort((a, b) => a.score > b.score ? -1 : 1)
                            soldiersResultArr.sort((a, b) => a.score > b.score ? -1 : 1)
                            playersResultArr.sort(a => a?.participatesInGame ? -1 : 1).sort((a, b) => a.score > b.score ? -1 : 1)
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
                                for (let i = 0; i < soldiersResultArr.length; i++) {
                                    const row = tablePlayer.insertRow(i + 2)
                                    row.insertCell(0).innerText = workersResultArr[i].id
                                    row.insertCell(1).innerText = workersResultArr[i].username
                                    row.insertCell(2).innerText = workersResultArr[i].role
                                    row.insertCell(3).innerText = workersResultArr[i]?.participatesInGame
                                    row.insertCell(4).innerText = workersResultArr[i].score
                                }
                                return tablePlayer
                            }

                             resultsArea.appendChild(createSoldierTable())
                            // resultsArea.appendChild(createWorkersTable())
                            // resultsArea.appendChild(createPlayersTable())
                        })
                    })
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
        let token = JSON.parse(localStorage.getItem(`userData`))?.token

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
                'Authorization': 'Bearer ' + token
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

    logout = () => {
        localStorage.removeItem('userData');
        this.props.history.push('/auth');
    }

    render(){return(
        <div id="soldier_page" align="center">

            <button onClick={this.logout}>Logout</button>
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
            {this.state.showResults === true &&
                <div id="results" style={{display: "flex", marginLeft: '300px', marginTop: '100px'}}>
                </div>}
        </div>

    )}
}
export default withRouter(Soldier)