CREATE TABLE LEAGUE(ID VARCHAR(12) PRIMARY KEY, LONG_NAME VARCHAR,  );
CREATE TABLE TEAM(REUTERS_CODE VARCHAR(3) PRIMARY KEY, LONG_NAME VARCHAR, LEAGUE_ID VARCHAR, YEAR INT );
CREATE TABLE INSTRUMENT(isin VARCHAR PRIMARY KEY,
home_team_id VARCHAR(12),
away_team_id VARCHAR(12),
league_id VARCHAR(3),
game_time TIMESTAMP,
active BOOLEAN);


INSERT INTO INSTRUMENT(ISIN,HOME_TEAM_ID, AWAY_TEAM_ID, LEAGUE_ID, GAME_TIME, ACTIVE) VALUES
('BRHTOT8AEPL', 'BRH', 'TOT', 'EPL', '2019-10-05 06:30:00', TRUE),
('NORAVA8AEPL', 'NOR', 'AVA', 'EPL', '2019-10-05 09:00:00', TRUE),
('MUNARS7AEPL', 'MUN', 'ARS', 'EPL', '2019-09-30 14:00:00', TRUE);


INSERT INTO TEAM(REUTERS_CODE, LONG_NAME, LEAGUE_ID) VALUES
('BOU', 'AFC Bournemouth', 'EPL'),
('ARS', 'Arsenal', 'EPL'),
('AVA', 'Aston Villa', 'EPL'),
('BRH', 'Brighton & Hove Albion', 'EPL'),
('BUR', 'Burnley', 'EPL'),
('CHE', 'Chelsea', 'EPL'),
('CRY', 'Crystal Palace', 'EPL'),
('EVE', 'Everton', 'EPL'),
('LEI', 'Leicester City', 'EPL'),
('LIV', 'Liverpool', 'EPL'),
('MCI', 'Manchester City', 'EPL'),
('MUN', 'Manchester United', 'EPL'),
('NEW', 'Newcastle United', 'EPL'),
('NOR', 'Norwich City', 'EPL'),
('SHU', 'Sheffield United', 'EPL'),
('SOU', 'Southampton', 'EPL'),
('TOT', 'Tottenham Hotspur', 'EPL'),
('WAT', 'Watford', 'EPL'),
('WHU', 'West Ham United', 'EPL'),
('WLV', 'Wolverhampton Wanderers', 'EPL');


INSERT INTO LEAGUE(ID, LONG_NAME) VALUES ('EPL', 'English Premier League'),
('LIGUE_1', 'Ligue 1'),
('SERIE_A', 'Serie A'),
('LA_LIGA', 'La Liga'),
('UCL', 'UEFA Champions League'),
('UEL', 'UEFA Europa League');