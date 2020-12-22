USE Armazem;

delete from QRCode;
delete from Armazem;
delete from Material;
delete from Palete where id = 4;

INSERT INTO QRCode (Codigo)
VALUES ('1&&Fruta&&5&&6'), ('2&&Pregos&&1&&3'),
('3&&Polvo&&5&&5'), ('4&&Carne&&10&&6');

INSERT INTO Armazem (ID) VALUES (1);

INSERT INTO Material (Designacao, Preco)
VALUES ('Fruta', 5.00), ('Pregos', 1.00),
('Polvo', 5.00), ('Carne', 10.00);

INSERT INTO Palete (ID, Armazem, QRCode, Peso, Corredor, Prateleira, Material) VALUES
(1, 1, '1&&Fruta&&5&&6', 6, 0, 0, 'Fruta'),
(2, 1, '2&&Pregos&&1&&3', 3, 1, 1, 'Pregos'),
(3, 1, '3&&Polvo&&5&&5', 5, 2, 4, 'Polvo'),
(4, 1, '4&&Carne&&10&&6', 6, -1, -1, 'Carne');
