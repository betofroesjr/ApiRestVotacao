# ApiRestVotacao

Para a fim de desenvolvimento foi escolhidos algumas tecnologias para atender os requisitos pedidos para criação de uma Api Rest. Foi escolhido primeiramente a linguagem de programação Java na versão 11, foi utilizado também o spring boot na versão 2.4.2, maven para gerenciamento das bibliotecas. Para solucionar o problema de persistência, para que os dados não se perca na reinicialização da aplicação, foi adotado o banco de dados H2. Esse é um banco de dados em memoria é foi escolhida por não precisar de instalação adicional na maquina onde ira ser executado. Foi feita toda a documentação da api utilizando o Swagger. Para fins de notificação foi utilizado o ActiveMq como mensageria. Todo o resultado da votação é publicado em um tópico, cada subscriber que se conectar nesse tópico irá receber o resultado da votação. Para testar a aplicação foi utilizado o Junit onde foi utilizado nos  teste de integração é testes unitário.

## 🚀 Começando

Esse projeto consiste na criação de uma api rest para votação.

### 📋 Pré-requisitos

Ira precisar da versão java 11, é do activeMq na versão: apache-activemq-5.16.1

link do activeMq: http://www.apache.org/dyn/closer.cgi?filename=/activemq/5.16.1/apache-activemq-5.16.1-bin.zip&action=download

para iniciar o activemq é mostrado na documentação.

## 📌 Versão

Nós usamos [gitgub](https://github.com/lucasfelicianods/) para controle de versão. Para as versões disponíveis, observe as [tags neste repositório](https://github.com/lucasfelicianods/ApiRestVotacao). 

## ✒️ Autores

* **Lucas Feliciano** - *Trabalho Inicial* - [lucasfelicianods](https://github.com/lucasfelicianods)
* **Lucas Feliciano** - *Documentação* - [lucasfelicianods](https://github.com/lucasfelicianods)

## 📄 Licença

Este projeto está sob a licença (sua licença) - veja o arquivo [LICENSE.md](https://github.com/lucasfelicianods/projeto/licenca) para detalhes.
