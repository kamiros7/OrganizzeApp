<p>Clone do app Organizze, perfomando as funções básicas do aplicativo, no qual foi implementado em linguagem Java, utilizando Firebase como banco de dados </p>

<p align="center">
  <a href="#-o-projeto">📓 O Projeto</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-tecnologias"> 🔧 Tecnologias</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-funcionalidades">💡 Funcionalidades</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-como-testar">🧪 Como testar</a>&nbsp;&nbsp;&nbsp;
</p>

## 📓 O Projeto 
<p> Foi criado com o intuito de aprimorar as habildides em desenvolvimento de aplicativos para android de forma nativa, correlacionado-se com o aprendizado bancos de dados não relacional, nesse caso, sendo o Firebase. </p>

## 🔧 Tecnologias
<p> As principais tecnologias utilizadas no projeto foram : </p>
  <div>
    <ul>
      <li>Java com Android Studio</li>
      <li>Firebase Authentication</li>
      <li>Firebase RealtimeDatabase</li>
      <li>Firebase Storage</li>        
     </ul>   
 </div>

 ## 💡 Funcionalidades

 <div>
    <ul>
      <li>Cadastro e Login de usuários (Autenticação via Email)</li>
      <li>Acionar receita e despesa de acordo com a data, valor e tipo</li>
      <li>Excluir receita ou despesa</li>
      <li>Navegar entre  as datas para verificar as receitas e despesa do mês/ano</li>    
      <li>Saldo total do usuário referente a todas as receitas e despesas inseridas</li>
     </ul>   
 </div>

 ## 🧪 Como testar

 Possuindo o Android Studio instalado, após isso, é preciso configurar o banco de dados, no caso sendo o Firebase, seguindo os passos abaixo :
  <div>
    <ul>
      <li>Criar conta no Firebase, caso não possua</li>
      <li>Criar o projeto no Firebase e linkar com o AndroidStudio, com a chave SHA-1 (gradle/app//tasks/andoird/signingReport) e o pacote base do projeto (AndroidManifests.xml) </li>
      <li>Configurar o Firebase Authentication, habilitando apenas o email</li>
      <li>Configurando o Firebase RealtimeDatabase, com as regras em que qualquer pessoa esteja liberado a alterar o banco de dados</li>    
      <li>Configurar o Firebase Storage</li>
      <li>Colocar o google-services.json na pasta indicada pelo Firebase</li>
      <li>Compilar o projeto e executar via emulador ou físicamente via smartphone ou tablet com sistema Android superiora 4.1( Jelly Bean)</li>
      <li>Obs: Testatado com MIUI Global 12.0.3</li>
     </ul>   
 </div>

