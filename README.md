# digital_certified_mail
This project discusses the implementation of the digitally certified mail scheme. The goal
of the project is to design and implement a full digital certified mail scheme where it has to fulfill
two main objectives, it has to require that a recipient sign for a message prior to receiving it to
acknowledge both sender and receiver, and also it has to stop the sender forging a receipt.
The project solution includes several exchanges of keys to make sure that when the sender sends
a valid receipt, the recipient will also receive a decrypted version of the message. In addition,
Both sides need to generate random DES keys for encryption and encrypt receipts with one
bogus message that is to be sent by the Sender. Oblivious transfer protocol will be used on both
sides for transferring the keys to each other. The project was built using Java through Netbean
8.2 for its implementation.

Oblivious Transfer Protocol(OT) is a mechanism for a receiver that wants to get a
specific text or message from a sender without letting the sender know its selection, and the
sender will send all encrypted messages based on the protocol oblivious to the receiver and it
gets as it wants. The reason behind it is the key security characteristic comes from the fact that
the receiver and sender will not have to specify selection and keys, but they can still exchange
what they want to transfer. As the result, oblivious transfer is symmetric, it can construct
efficient 𝑂𝑇 and from . In this project, will be the right approach for setting 1
𝑛 𝑂𝑇 𝑘
𝑛 𝑂𝑇 1
2 𝑂𝑇 1
2
up the foundation of transferring messages. Further discussion will be discussed in the design
and implementation parts.

Partial Secrets Exchange Subprotocol(PSE) will also be used for the transaction protocols
for preventing the sender from forge a receipt. There will be two parties to the subprotocol and it
will be called A and B. The proposal is that A holds 2n secrets that are all recognizable by B
when B also holds 2n secrets that are all recognizable by A. The secrets protocol are assumed to
be binary strings of length l. The secrets of both sides are subdivided into pairs. A [B] will
constructively know one of B’s [A’s] pairs if there exists an i, 1≤ i ≤ n, so that A [B] can
constructively compute both bi and bn+i and vice versa B[A] (ai and an+i). Exchanging effective
knowledge of any one pair of secrets is the goal of this specific subprotocol. This subprotocol
will be referred to as the Partial Secret Exchange (PSE) subprotocol in the following report.
