import React, { Component } from 'react'
import ContractTable from './ContractTable'
import { Icon } from 'semantic-ui-react'
import { connect } from 'react-redux';
const { League } = require('../proto/market_data_pb.js');



class LeagueMenu extends Component {
    state = {
        activeItem: League.ENGLISH_PREMIER_LEAGUE
    }

    render(props) {
        const { activeItem } = this.state
        const classes = (bool) => [
            ...(bool ? ['highlighted'] : []),
            'col',
            'clickable'
        ].join(' ');

        return (
            <React.Fragment>
                <div className='row'>
                    <div onClick={() => this.setState({ activeItem: League.ENGLISH_PREMIER_LEAGUE })} className={classes(this.state.activeItem === League.ENGLISH_PREMIER_LEAGUE)}><i className="gb flag" />Premier League</div>
                    <div onClick={() => this.setState({ activeItem: League.UEFA_CHAMPIONS_LEAGUE })} className={classes(this.state.activeItem === League.UEFA_CHAMPIONS_LEAGUE)} ><i className="eu flag" />Champions League</div>
                    <div onClick={() => this.setState({ activeItem: League.SERIE_A })} className={classes(this.state.activeItem === League.SERIE_A)}><i className="it flag" />Serie A</div>
                    <div onClick={() => this.setState({ activeItem: League.LA_LIGA })} className={classes(this.state.activeItem === League.LA_LIGA)}><i className="es flag" />La Liga</div>
                    <div onClick={() => this.setState({ activeItem: League.LIGUE_1 })} className={classes(this.state.activeItem === League.LIGUE_1)}><i className="fr flag" />Ligue 1</div>
                    <div onClick={() => this.setState({ activeItem: League.UEFA_EUROPA_LEAGUE })} className={classes(this.state.activeItem === League.UEFA_EUROPA_LEAGUE)}><i className="eu flag" />Europa League</div>
                </div>
                <div className='row'>
                    <div className='col'><ContractTable contracts={this.props.contracts} /></div>
                </div>
            </React.Fragment >
        )
    }
}

const mapStateToProps = (state) => ({ contracts: state.marketData.instrumentsByIsin })


export default connect(mapStateToProps)(LeagueMenu);