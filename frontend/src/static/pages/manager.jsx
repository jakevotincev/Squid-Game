// todo
import React, {Component} from 'react';
import {useRef} from 'react';
import {Client} from "@stomp/stompjs";
import {useState} from 'react';
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';
import "./pagestyle.css";

class Manager extends Component {


    state = {
        glavniyAnswer: null,
        crit: '',
        numb: '',
        slovo: '',
        allAnketasIsCollected: false,
        answerStatus: null,
        showLunch: false,
        showResults: false
    }


    handleChange = (event) => {
        // 👇 Get input value from "event"
        this.setState({crit: event.target.value});


    }
    handleChange2 = (event) => {
        // 👇 Get input value from "event"
        this.setState({numb: event.target.value});

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
            const headers = {Authorization: 'Bearer ' + localStorage.getItem('manager')}
            this.client.subscribe('/manager/messages', message => {

                console.log(JSON.parse(message.body));
                this.setState({glavniyAnswer: message.body});
                let sad = JSON.parse(message.body);
                if (sad.type === 'RESULTS_READY') {
                    this.setState({showResults: true})
                    fetch('http://localhost:8080/account/2/results', {
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
                }
                if (sad.type === "ALL_FORMS_COLLECTED") {
                    this.setState({allAnketasIsCollected: true});
                }
                if (sad.type === 'FORMS_SELECTION_COMPLETED') {
                    this.setState({showLunch: true});
                }
                this.setState({answerStatus: sad.confirm});
                // console.log(this.state.answerStatus);
                if (sad.confirm) {
                    this.state.slovo = 'Критерии утверждены';
                } else {
                    this.state.slovo = 'Переделывай, причина отказа : ';
                    this.state.slovo = this.state.slovo.concat(sad.declineReason);
                }

            }, headers);
            this.client.subscribe('/user/worker/messages', message => {
                console.log(JSON.parse(message.body));
            }, headers)

        } else {
            // Queue a retry
            setTimeout(() => {
                this.handleSend()
            }, 100)
        }
    };

    clickHandler = () => {
        console.log(this.state.crit);
        console.log(this.state.numb);
        const criteriaMsg = {
            criteria: {
                playersNumber: this.state.numb,
                criteria: this.state.crit,
                gameId: 1
            }
        }

        this.client.publish({
            destination: '/app/sendCriteriaToGlavniy',
            headers: {Authorization: 'Bearer ' + localStorage.getItem('glavniy')},
            body: JSON.stringify(criteriaMsg)
        });

    }
    sendAnketasHandler = () => {
        fetch('http://localhost:8080/sendCriteriaAndFormsToWorkers', {
            headers: {
                // "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        }).then(
            //todo: добавить что нибудь
        )

    }

    startLunch = () => {
        fetch('http://localhost:8080/startLunch', {
            headers: {
                // "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }

    startRoundPreparation = () => {
        fetch('http://localhost:8080/startPrepareRound', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }
    startTraining = () => {
        fetch('http://localhost:8080/startTraining', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }
    stopTraining = () => {
        fetch('http://localhost:8080/stopTraining', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }
    startCleaning = () => {
        fetch('http://localhost:8080/startCleaning', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }
    stopCleaning = () => {
        fetch('http://localhost:8080/stopCleaning', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('manager')
            },
            method: 'GET',
            mode: 'cors'
        })
    }

    render() {
        return (

            <div id="manager_page">
                <h1>This is manager page</h1>
                {this.state.showResults === false &&
                    <div>
                        <div className="Criteria_Message" id="criteria_message">
                            <h3>Критерии</h3>
                            <br/>
                            <form>
                                <label>Критерии отбора: </label>
                                <input type="text" id="crit" name="crit" placeholder="Введите количество участников"
                                       onChange={this.handleChange}/>

                                <br/>

                                <label>Количество участников: </label>
                                <input type="text" id="numb" name="numb" placeholder="Введите кол-во участников"
                                       onChange={this.handleChange2}/>
                            </form>
                            <br/>
                            <button type="submit" onClick={this.clickHandler}>Отправить</button>
                            <div class="bossmsg">
                                <Popup trigger={<button id="boss_msg_btn"> Сообщение от босса </button>}
                                       position="right centre">
                                    <p id="boss_message">
                                        Ваш босс ответил : {this.state.slovo}
                                    </p>
                                </Popup>
                            </div>
                        </div>
                        <br/><br/><br/>

                        {this.state.allAnketasIsCollected === true &&
                            // !(this.state.answerStatus) &&
                            <div>
                                <button type="submit" onClick={this.sendAnketasHandler}>Отправить анкеты рабочим
                                </button>
                            </div>

                        }

                        {
                            this.state.showLunch &&
                            <div>
                                <div>
                                    <h2>Все игроки отобраны</h2>
                                </div>
                                <div>
                                    <button type="submit" onClick={this.startLunch}>Отдать приказ о начале обеда
                                    </button>
                                </div>
                            </div>
                        }

                        <div>
                            <button type="submit" onClick={this.startRoundPreparation}>Отдать приказ о старте подготовки
                                раунда
                            </button>
                        </div>
                        <br/>
                        <div>
                            <button type="submit" onClick={this.startTraining}>Отдать приказ о начале тренировок
                                солдат
                            </button>
                        </div>
                        <br/>
                        <div>
                            <button type="submit" onClick={this.stopTraining}>Отдать приказ о прекращении тренировок
                                солдат
                            </button>
                        </div>
                        <div>
                            <button type="submit" onClick={this.startCleaning}>Отдать приказ о начале уборки рабочим
                            </button>
                        </div>
                        <br/>
                        <div>
                            <button type="submit" onClick={this.stopCleaning}>Отдать приказ о прекращении уборки
                                рабочих
                            </button>
                        </div>
                    </div>}
                {this.state.showResults === true &&
                    <div id="results" style={{display: "flex", marginLeft: '300px', marginTop: '100px'}}>
                    </div>}
            </div>


        );
    }
}

export default Manager;

