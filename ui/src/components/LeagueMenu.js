import React, { Component } from 'react'
import ContractTable from './ContractTable'
import { Icon } from 'semantic-ui-react'
const { League } = require('../proto/market_data_pb.js');



class LeagueMenu extends Component {
    state = {
        selectedLeague: League.ENGLISH_PREMIER_LEAGUE
    }

    render(props) {
        const { selectedLeague } = this.state
        const classes = (bool) => [
            ...(bool ? ['highlighted'] : []),
            'col',
            'clickable'
        ].join(' ');

        return (
            <React.Fragment>
                <div className='row'>
                    <div onClick={() => this.setState({ selectedLeague: League.ENGLISH_PREMIER_LEAGUE })} className={classes(this.state.selectedLeague === League.ENGLISH_PREMIER_LEAGUE)}><i className="gb flag" />Premier League</div>
                    <div onClick={() => this.setState({ selectedLeague: League.UEFA_CHAMPIONS_LEAGUE })} className={classes(this.state.selectedLeague === League.UEFA_CHAMPIONS_LEAGUE)} ><i className="eu flag" />Champions League</div>
                    <div onClick={() => this.setState({ selectedLeague: League.SERIE_A })} className={classes(this.state.selectedLeague === League.SERIE_A)}><i className="it flag" />Serie A</div>
                    <div onClick={() => this.setState({ selectedLeague: League.LA_LIGA })} className={classes(this.state.selectedLeague === League.LA_LIGA)}><i className="es flag" />La Liga</div>
                    <div onClick={() => this.setState({ selectedLeague: League.LIGUE_1 })} className={classes(this.state.selectedLeague === League.LIGUE_1)}><i className="fr flag" />Ligue 1</div>
                    <div onClick={() => this.setState({ selectedLeague: League.UEFA_EUROPA_LEAGUE })} className={classes(this.state.selectedLeague === League.UEFA_EUROPA_LEAGUE)}><i className="eu flag" />Europa League</div>
                </div>
                <div className='row'>
                    <div className='col'><ContractTable league={this.state.selectedLeague} /></div>
                </div>
            </React.Fragment >
        )
    }
}


export default LeagueMenu;