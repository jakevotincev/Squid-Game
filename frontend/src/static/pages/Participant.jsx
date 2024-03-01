import React, { Component } from 'react';
import {Client} from "@stomp/stompjs";
import "./pagestyle.css"

class Participant extends Component{
    state = {
        nickname: "",
        content: "",
        playerId: null,
        showAnketa: true,
        showQuiz: false,
        quiz: null
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

            this.client.subscribe('/user/player/messages', message => {
                console.log('message',JSON.parse(message.body));
                let sad = JSON.parse(message.body);
                if (sad.type === 'GAME_STARTED'){
                    this.setState({showAnketa: false});
                    this.setState({showQuiz: true});
                    fetch('http://localhost:8080/questions',{
                        headers: {
                            "Content-Type": "application/json"
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
            })
            this.client.subscribe('/player/messages', message => {
                console.log('govnomapping',JSON.parse(message.body));

            })
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

            <h1>This is participant page</h1>
            <button id="connect" className="btn btn-default" type="submit" onClick={this.componentDidMount}>Connect</button>
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
        </div>
    );
    }
}
export default Participant;