import React, { Component } from 'react'
import ContractTable from './ContractTable'
import { Menu } from 'semantic-ui-react'
const { League } = require('../proto/market_data_pb.js');
export default class LeagueMenu extends Component {
    state = {
        activeItem: 'EPL'
    }

    ENGLISH_PREMIER_LEAGUE = 1;
    UEFA_CHAMPIONS_LEAGUE = 2;
    UEFA_EUROPA_LEAGUE = 3;
    LA_LIGA = 4;
    LIGUE_1 = 5;
    SERIE_A = 6;

    render() {
        const { activeItem } = this.state

        return (
            <React.Fragment>
                <table className='leagueMenu'>
                    <thead>
                        <tr>
                            <th className='league'></th>
                            <th className='contracts'></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr
                            key={League.ENGLISH_PREMIER_LEAGUE}
                            onClick=
                            {() => this.setState({ activeItem: League.ENGLISH_PREMIER_LEAGUE })}>
                            <td className={this.state.activeItem == League.ENGLISH_PREMIER_LEAGUE ? 'active-league' : 'inactive-league'} ><i className="gb flag" /> {'English Premier League'}</td>
                            <td rowSpan="0"><ContractTable contracts={[]} /></td>
                        </tr>
                        <tr
                            key={League.UEFA_CHAMPIONS_LEAGUE}
                            onClick=
                            {() => this.setState({ activeItem: League.UEFA_CHAMPIONS_LEAGUE })}>
                            <td><i className="eu flag" /> {'UEFA Champions League'}</td>
                        </tr>
                        <tr
                            key={League.SERIE_A}
                            onClick=
                            {() => this.setState({ activeItem: League.SERIE_A })}>
                            <td><i className="it flag" /> {'Serie A'}</td>
                        </tr>
                        <tr
                            key={League.UEFA_EUROPA_LEAGUE}
                            onClick=
                            {() => this.setState({ activeItem: League.UEFA_EUROPA_LEAGUE })}>
                            <td><i className="eu flag" /> {'UEFA Europa League'}</td>
                        </tr>
                        <tr
                            key={League.LA_LIGA}
                            onClick=
                            {() => this.setState({ activeItem: League.LA_LIGA })}>
                            <td><i className="es flag" /> {'La Liga'}</td>
                        </tr>
                        <tr
                            key={League.LIGUE_1}
                            onClick=
                            {() => this.setState({ activeItem: League.LIGUE_1 })}>
                            <td><i className="fr flag" /> {'Ligue 1'}</td>
                        </tr>
                    </tbody>
                </table>
            </React.Fragment>
        )
    }
}