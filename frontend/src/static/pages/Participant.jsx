import React, { Component } from 'react';
import {Client} from "@stomp/stompjs";
import "./pagestyle.css"
import {useLocation} from "react-router-dom";
export function withRouter(Children) {
    return(props)=>{
        const location = useLocation()
        return <Children {...props} location = {location}/>
    }
}
class Participant extends Component{
    state = {
        nickname: "",
        content: "",
        playerId: null,
        showAnketa: true,
        showQuiz: false,
        quiz: null,
        showFoodQuiz: false,
        showResults: false
    }
    handleChange = (event) => {
        // 👇 Get input value from "event"
        this.setState({nickname : event.target.value});


    }
    handleChange2 = (event) => {
        // 👇 Get input value from "event"
        this.setState({content : event.target.value});


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

                this.setState({playerId: data.id})
                console.log(JSON.stringify(data))
            })
            }

        )
        console.log(this.state.playerId);
        if (this.props.location.state !== ""){
            // setTimeout(() => { this.componentDidMount() }, 2000);
            console.log('Component did mount');
            console.log(this.state.nickname);
            // The compat mode syntax is totally different, converting to v5 syntax
            // Client is imported from '@stomp/stompjs'
            this.client = new Client();

            this.client.configure({
                brokerURL: this.getNickname(),
                connectHeaders: {
                    Authorization: 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
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

            this.client.activate();}

    }
    handleSend = () => {
        if (this.client.webSocket.readyState === WebSocket.OPEN) {
            const headers = { Authorization: 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)}
            // this.client.subscribe('/glavniy/messages', message => {
            //     console.log(JSON.parse(message.body));
            // }, headers);

            this.client.subscribe('/user/player/messages', message => {
                console.log('message',JSON.parse(message.body));
                let sad = JSON.parse(message.body);
                if (sad.type === 'GAME_STARTED'){
                    this.setState({showAnketa: false});
                    this.setState({showQuiz: true});

                    let url = 'http://localhost:8080/account/'.concat(this.state.playerId).concat('/questions');
                    console.log(url);
                    fetch(url,{
                        headers: {
                            "Content-Type": "application/json",
                            'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                        },
                        method: 'GET',
                        mode: 'cors'
                    }).then(res => res.json().then(data =>{
                        console.log('data', JSON.stringify(data))
                        console.log('dataobj',data)
                        let quizz = data;
                        let quizArea = document.getElementById('quiz');
                        // todo remove hardcode
                        let isAnswerCorrect;
                        let counter = quizz.length
                        for (let i=0; i<counter;i++){
                            let curr_id = quizz[i].id;
                            let curr_question = quizz[i].question;
                            let curr_answers = quizz[i].answers;
                            let curr_otvet;
                            let str = "Вопрос №"+curr_id+".  "+curr_question;
                            let emptyStr = document.createElement('br');
                            let text = document.createTextNode(str);
                            let radioBtnDiv = document.createElement('div');
                            let checkAnswerBtn = document.createElement('button');
                            checkAnswerBtn.type="submit";
                            checkAnswerBtn.innerHTML="Отправить ответ";
                            console.log("curr_answers",curr_answers);

                            for (let i=0; i<curr_answers.length;i++){
                                let label = document.createElement("label");
                                label.innerText = curr_answers[i];
                                let input = document.createElement("input");
                                input.type = "radio";
                                input.name = "colour";
                                input.value = curr_answers[i];
                                const radioButtons = document.querySelectorAll('input[name="colour"]');
                                input.addEventListener('click', (event) => {
                                    if (event.currentTarget.checked){
                                        curr_otvet = curr_answers[i];
                                    }
                                });

                                label.appendChild(input);
                                radioBtnDiv.appendChild(label);
                                radioBtnDiv.appendChild(emptyStr);
                                radioBtnDiv.appendChild(emptyStr);
                                radioBtnDiv.appendChild(checkAnswerBtn);

                            }
                            quizArea.appendChild(text);
                            quizArea.appendChild(emptyStr);
                            quizArea.appendChild(radioBtnDiv);
                            quizArea.appendChild(emptyStr);
                            checkAnswerBtn.addEventListener('click',(ev)=>{
                                console.log('curr_otvet',curr_otvet);
                                let destination = "http://localhost:8080/checkAnswer/";
                                destination = destination.concat(this.state.playerId);
                                destination = destination.concat('/');
                                let quest_id = curr_id.toString();
                                quest_id = quest_id.concat('/');
                                destination = destination.concat(quest_id);
                                destination = destination.concat(curr_otvet);
                                console.log("dist",destination);

                                fetch(destination,{
                                    headers: {
                                        // "Content-Type": "application/json"
                                        'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                                    },
                                    method: 'GET',
                                    mode: 'cors'
                                }).then(
                                    res => res.text().then(resData => {
                                        isAnswerCorrect = resData;
                                        console.log(isAnswerCorrect);
                                        if (isAnswerCorrect === 'true')
                                        {  console.log('answer true')

                                        }
                                        else if(isAnswerCorrect === 'false'){
                                            this.setState({quiz: 'Вы ошиблись, скоро вас убьют'});
                                            this.setState({showQuiz: false})
                                            console.log('answer false')

                                        }
                                    })
                                )
                            })

                        }
                        console.log(this.state.quiz)
                    }))
                    console.log('quizzz',this.state.quiz)
                }
                if (sad.type === 'FOOD_PREPARED') {
                    this.setState({showFoodQuiz: true})
                    let answersCounter = 0
                    let url = 'http://localhost:8080/account/'
                    url = url.concat(this.state.playerId)
                    url = url.concat('/questions')
                    console.log(url, 'url')
                    fetch(url,{
                        headers: {
                            "Content-Type": "application/json",
                            'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                        },
                        method: 'GET',
                        mode: 'cors'
                    }).then(res => {res.json().then(data =>{
                        console.log(JSON.stringify(data))
                        const questionsData = data
                        let quizArea = document.getElementById('foodquiz');
                        let counter = questionsData.length
                        let isAnswerCorrect;
                        for (let i=0; i<counter;i++) {
                            let curr_id = questionsData[i].id;
                            let curr_question = questionsData[i].question;
                            let curr_answers = questionsData[i].answers;
                            let curr_otvet;
                            let str = "Вопрос №" + i + ".  " + curr_question;
                            let emptyStr = document.createElement('br');
                            let text = document.createTextNode(str);
                            let radioBtnDiv = document.createElement('div');
                            let checkAnswerBtn = document.createElement('button');
                            checkAnswerBtn.type = "submit";
                            checkAnswerBtn.innerHTML = "Отправить ответ";
                            console.log("curr_answers", curr_answers);
                            for (let i = 0; i < curr_answers.length; i++) {
                                let label = document.createElement("label");
                                label.innerText = curr_answers[i];
                                let input = document.createElement("input");
                                input.type = "radio";
                                input.name = "colour";
                                input.value = curr_answers[i];
                                const radioButtons = document.querySelectorAll('input[name="colour"]');
                                input.addEventListener('click', (event) => {
                                    if (event.currentTarget.checked) {
                                        curr_otvet = curr_answers[i];
                                    }
                                });

                                label.appendChild(input)
                                radioBtnDiv.appendChild(label)
                                radioBtnDiv.appendChild(emptyStr)
                                radioBtnDiv.appendChild(emptyStr)
                                radioBtnDiv.appendChild(checkAnswerBtn)

                            }
                            quizArea.appendChild(text)
                            quizArea.appendChild(emptyStr)
                            quizArea.appendChild(radioBtnDiv)
                            quizArea.appendChild(emptyStr)
                            checkAnswerBtn.addEventListener('click', (ev) => {
                                // answersCounter +=1
                                console.log('curr_otvet', curr_otvet);
                                let destination = "http://localhost:8080/checkAnswer/";
                                destination = destination.concat(this.state.playerId);
                                destination = destination.concat('/');
                                let quest_id = curr_id.toString();
                                quest_id = quest_id.concat('/');
                                destination = destination.concat(quest_id);
                                destination = destination.concat(curr_otvet);
                                console.log("dist", destination);
                                fetch(destination, {
                                    headers: {
                                        // "Content-Type": "application/json"
                                        'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                                    },
                                    method: 'GET',
                                    mode: 'cors'
                                }).then(
                                    res => res.text().then(resData => {
                                        answersCounter = answersCounter + 1
                                        isAnswerCorrect = resData;
                                        console.log(isAnswerCorrect);
                                        if (isAnswerCorrect === 'true') {
                                            console.log('answer true')

                                        } else if (isAnswerCorrect === 'false') {
                                            console.log('answer false')

                                        }
                                        console.log(answersCounter, 'counter')
                                        if (answersCounter === 3) {
                                            console.log('tuta')
                                            this.setState({showFoodQuiz: false})
                                            let urlReady = 'http://localhost:8080/account/'
                                            urlReady = urlReady.concat(this.state.playerId)
                                            urlReady = urlReady.concat('/readyToGame')
                                            fetch(urlReady, {
                                                headers: {
                                                    // "Content-Type": "application/json"
                                                    'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                                                },
                                                method: 'GET',
                                                mode: 'cors'
                                            })
                                        }
                                    })
                                )
                            })
                        }
                    })
                    })
                }
                if (sad.type === 'QUALIFIED_TO_NEXT_ROUND_MESSAGE'){
                    this.setState({quiz: 'Вы прошли в следующий раунд'})
                    this.setState({showQuiz: false})
                }
                if (sad.type === 'KICKED_FROM_GAME_MESSAGE') {
                    this.setState({showAnketa: false});
                    this.setState({quiz: 'Вы не прошли отбор для участия'})
                }
                if (sad.type === 'QUALIFIED_TO_GAME_MESSAGE') {
                    this.setState({showAnketa: false});
                    this.setState({quiz: 'Вы  прошли отбор для участия в игре'})
                }
                if (sad.type === 'PLAYER_KILLED_MESSAGE') {
                    this.setState({quiz: 'ВАС убили, ВЫ проиграли'})
                }
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

                            // resultsArea.appendChild(createSoldierTable())
                            // resultsArea.appendChild(createWorkersTable())
                            resultsArea.appendChild(createPlayersTable())
                        })
                    })
                }
            }, headers)
            this.client.subscribe('/player/messages', message => {
                console.log('govnomapping',JSON.parse(message.body));

            }, headers)
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
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
            },
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(anketa) // body data type must match "Content-Type" header

        })
    }
    render(){return(
        <div id="participant_page" align="center">
            {/*{this.state.showQuiz === false &&*/}
            <div>
            <h1 id="page_title">This is player page</h1>
            {/*<button id="connect" className="btn btn-default" type="submit" onClick={this.componentDidMount}>Connect</button>*/}
            {this.state.showAnketa === true &&
                <div class="participant" id="anketa" align="center">
                    <h3>Пожалуйста, заполните анекту участника</h3>
                    <label>Никнейм: </label>
                    <input type="text" id="nick" name="nick" placeholder="Введите ваш никнейм" onChange={this.handleChange}/>
                    <br/>
                    <label>Ваша анкета: </label>
                    <input type="text" id="anketa" name="anketa" placeholder="Введите текст вашей анкеты участника " height="400" width="200" onChange={this.handleChange2}/>
                    <br/><br/>
                    <button type="submit" onClick={this.clickHandler}>Отправить</button>
                </div>
            }
            {this.state.showQuiz === true &&
                <div id="quiz">
                    Игра началась<br/>
                </div>}
            {this.state.quiz !=null &&
                <div>
                    {this.state.quiz}
                </div>}
            {this.state.showFoodQuiz === true &&
                <div id="foodquiz"></div>}
            </div>
            {this.state.showResults === true &&
            <div id="results" style={{display: "flex", marginLeft: '300px', marginTop: '100px'}}>
            </div>}
        </div>
    );
    }
}
export default withRouter(Participant)