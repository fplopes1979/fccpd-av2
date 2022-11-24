# fccpd-av2
AV2 da disciplina de Sistemas Distribuídos

Prezado Rafael,

Como não haverá a apresentação do trabalho, resolvemos escrever um pequeno texto relatando o desenvolvimento do nosso trabalho para que fique mais claro
pra ti o funcionamento do código na hora que você for avaliá-lo.

Anes de tudo, queremos dizer que foi muito bacana realizar essa atividade. Apesar das dificuldades com Java que nossa turma tem, é unânime que aprendemos
muito com você e esse trabalho foi mais uma oportunidade para desenvolvermos essa habilidade e ganhar familiaridade com códigos Java.

Partindo agora para o código em si: o que fizemos foi, primeiro, criar 4 projetos Spring, um para cada componente do nosso produto (produtor, auditoria,
localização e consumidor). Em cada um, adicionamos como dependência o Spring for RabbitMQ. Com os projetos criados, fizemos a importação dos mesmos dentro
de um projeto no IntelliJ e ficamos assim com 4 módulos distintos. Dentro de cada módulo, configuramos o arquivo application.properties, na pasta resources,
utilizando os dados gerados no site CloudAMQP.com, que foi por onde acompanhamos e gerenciamos as atividades executadas. 

Falando rapidamente de cada componente:

- O componente "produtor" ficou responsável pela criação da exchange, das filas e dos bindings necessários para a comunicação com os outros componentes. 
ELE DEVE SER O PRIMEIRO A SER EXECUTADO quando da inicialização do programa. Além disso, é através dele que, obviamente, as mensagens são produzidas e enviadas para o MoM. 

- O componente "auditoria", conforme requisitado, está configurado para receber todas as mensagens geradas pelo programa.

- O componente "localização", por outro lado, está programado para receber APENAS mensagens cujo evento seja "power-on" ou "power-off"

- O componente "consumidor" irá receber as mensagens que lhe são cabidas, de acordo com o que você verá no código, e não há, na prática, limites para
quantas cópias do mesmo podem ser executadas simultaneamente (todas as cópias estão recebendo a mesma mensagem)

De início, nós pensamos em utilizar uma exchange do tipo 'fanout' e ligar uma fila a ela que iria receber todas as mensagens, fila essa que seria
direcionada ao componente de auditoria. Ao componente de localização, ligariamos uma outra exchange do tipo 'direct' e utilizaríamos as strings "power-on" 
e "power-off" como chaves de roteamento para a entrega dessas mensagens a ele. E essa lógica foi implementada nas primeiras versões do código, funcionando
de forma adequada.

Porém, logo vimos que essa solução tinha alguns problemas. Primeiro, a seleção das mensagens a serem entregues não estava sendo feita diretamente pelo 
RabbitMQ, mas sim através de algumas linhas de estruturas condicionais dentro do código do componente produtor. Não era esse o comportamento esperado, 
apesar de funcionar. E segundo, quando começamos a desenvolver o componente consumidor, chegamos em um momento de "bloqueio criativo" e não estávamos
sabendo como fazer com que várias instâncias desse componente recebesse as mesmas mensagens ao mesmo tempo. Até conseguimos fazer com que o RabbitMQ 
mandasse mensagens para vários consumidores, mas ele estava fazendo isso num esquema 'round robin', que, segundo nossas pesquisas, é o comportamento
padrão dele nesse tipo de cenário e isso não atendia às nossas necessidades. 

Dessa forma, demos uma guinada na direção das nossas idéias e resolvemos pesquisar mais, achar outra maneira de atingirmos os objetivos da tarefa de
forma mais otimizada e cumprindo todos os requisitos. Foi então que, depois de alguns dias de estudo, concluimos que o que iria atender nossas
necessidades seria a utilização de um outro tipo de exchange, uma exchange do tipo 'topic'. Quando refizemos o código para fazê-lo utilizar esse tipo
de exchange, percebemos que não precisaríamos de duas exchanges, uma 'fanout' e outra 'direct'; a exchange topic é bastante poderosa e versátil e ela
sozinha resolveu todos os nossos problemas. Passamos a ligar a fila do componente de auditoria com uma chave de roteamento "#", e assim ele passou a 
receber todas as mensagens que trafegavam no programa. Para o componente de localização, caso o evento fosse do tipo "power-on" ou "power-off", nós
passamos a mandar a mensagem com a chave de roteamento "local.*", e assim direcionamos esses dois tipos de evento ao componente adequado, com o de
auditoria recebendo essas mensagens também, já que, como disse, a sua fila estava utilizando a routing key "#", que faz o componente receber todas as
mensagens que trafegam pela exchange. Faltava apenas o componente consumidor.

Esse foi um pouco mais trabalhoso e exigiu um pouco de perspicácia do grupo. A questão é que, depois de muito debate e das orientações em sala de aula,
nós chegamos à conclusão que o que resolveria nosso problema seria a criação de uma fila para cada componente consumidor em execução e cada fila dessa
estaria ligada à exchange topic pela mesma chave de roteamento, para a qual utlizamos a string "consumer.*". Mas como seriam essas filas? Porque um dos
requisitos do trabalho era que, enquanto não houvessem consumidores em execução, nenhum consumo relacionado a eles poderia existir. Logo chegamos à
solução: filas temporárias. Filas que são criadas quando da inicialização do componente e que se autodeletam quando o mesmo para de executar. Isso 
pode ser feito através de um parâmetro booleano na hora da criação da fila, quando retornamos essa nova fila na função de criação. Setando esse campo
de autodelete para TRUE, conseguimos esse efeito. Mais um problema resolvido. Mas ainda faltava um.

Como disse antes, teríamos que ter uma fila para cada componente consumidor em execução e cada fila dessa precisa ter um nome diferente para que seja
possível individualizá-las. Depois de mais alguns momentos de debate e estudo, decidimos pela solução de gerar RANDOMICAMENTE o nome dessas filas no 
momento da criação das mesmas. Assim, adicionamos ao código do consumidor uma função que randomiza os caracteres 

ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789

e cria uma string de tamanho 10 composta de uma das possíveis combinações desses caracteres. Vemos que as possibilidades de criação de diferentes nomes
de filas com esse método são de ordem astronômica, o que praticamente nos garante que duas filas não terão o mesmo nome. E assim, cada fila dessa se
liga à mesma exchange topic que criamos e recebem as mesmas mensagens de acordo com sua chave de roteamento. Ao se encerrar a execução do componente,
essas filas deixam de existir e nenhum consumo é realizado. 

E, assim, achamos que todos os requisitos do trabalho foram atendidos de forma adequada. Esperamos que esse pequeno passeio por nossa jornada de desen-
volvimento ajude a deixar o código mais fácil de ler. Agradecemos mais uma vez a você, Rafael, pelo aprendizado e pela amizade demonstrada ao longo do 
semestre. Desejamos muita saúde pra você e sua família e sucesso em todos os seus projetos futuros.

Um abraço dos seus alunos,

Fábio Lopes, Bernardo Queiroz e Danilo Aragão



