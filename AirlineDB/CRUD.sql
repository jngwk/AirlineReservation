select * from categories;
select * from users;
select * from posts ORDER BY postID DESC;

desc users;
desc posts;

insert users
values ('sample', '1234', 'sampleName', 'sample@dino.com', 'M');

INSERT INTO categories (categoryNames) VALUES
('Tyrannosaurs'),
('Sauropods'),
('Velociraptors'),
('Triceratops'),
('Stegosaurs'),
('Pterosaurs'),
('Ankylosaurs'),
('Carnivores'),
('Herbivores'),
('Paleontology'),
('Dinosaur Art'),
('Dinosaur Toys'),
('Dinosaur Movies'),
('Dinosaur Facts'),
('Dinosaur Fiction');

INSERT INTO users (userId, userPwd, name, email, sex) VALUES
('user123', 'password123', 'John Doe', 'john@example.com', 'M'),
('dinoLover99', 'dinolove', 'Alice Smith', 'alice@example.com', 'F'),
('paleoExplorer', 'fossil123', 'Robert Johnson', 'robert@example.com', 'M'),
('dinoArtist22', 'artpass', 'Emily Brown', 'emily@example.com', 'F'),
('movieBuffDino', 'jurassic123', 'Michael Wilson', 'michael@example.com', 'M'),
('dinoFanatic88', 'dinos4life', 'Jessica Lee', 'jessica@example.com', 'F'),
('roarMaster', 'rawr456', 'David Clark', 'david@example.com', 'M'),
('rexRuler', 'trex123', 'Sarah Adams', 'sarah@example.com', 'F'),
('dinoToyCollector', 'toylover', 'Daniel Taylor', 'daniel@example.com', 'M'),
('flyingDinoExpert', 'pterodactyl', 'Olivia Moore', 'olivia@example.com', 'F'),
('ankleBiter', 'armor123', 'Matthew Jones', 'matthew@example.com', 'M');

INSERT INTO posts (userId, categoryId, title, content) VALUES
('user123', 1, 'Ferocious Tyrannosaurs', 'Let''s discuss the hunting habits of Tyrannosaurs!'),
('dinoLover99', 3, 'Admiring Velociraptors', 'Velociraptors were such fascinating creatures. Who else is intrigued by their intelligence?'),
('paleoExplorer', 10, 'Latest Paleontological Discoveries', 'Exciting new fossils uncovered in the Gobi Desert. Let''s dive into the details!'),
('dinoArtist22', 11, 'Showcasing Dinosaur Art', 'Check out my latest painting of a Triceratops! What do you think?'),
('movieBuffDino', 13, 'Jurassic Park Rewatch', 'Just watched Jurassic Park for the 100th time! Let''s discuss the accuracy of the dinosaur depictions.'),
('dinoFanatic88', 14, 'Fun Dinosaur Facts', 'Did you know that the Stegosaurus had a brain the size of a walnut? Share your favorite dinosaur facts here!'),
('roarMaster', 8, 'Carnivores vs. Herbivores', 'Debate time: which dinosaur group do you think was more dominant, carnivores or herbivores?'),
('rexRuler', 4, 'Triceratops: The Horned Dinosaur', 'Let''s talk about the iconic Triceratops and its unique features.'),
('dinoToyCollector', 12, 'Collecting Dinosaur Toys', 'Show off your dinosaur toy collection! Here are my latest additions.'),
('flyingDinoExpert', 6, 'Mysteries of Pterosaurs', 'Pterosaurs were the rulers of the skies. Let''s explore their fascinating adaptations.'),
('ankleBiter', 7, 'Ankylosaurs: The Tank Dinosaurs', 'Ankylosaurs were armored giants. Discuss their defensive strategies here.');

INSERT INTO posts (userId, categoryId, title, content) VALUES
('sample', 9, 'The Fascinating World of Herbivores', 'Let''s explore the diverse diets and behaviors of herbivorous dinosaurs!');


DELETE FROM posts;

commit;