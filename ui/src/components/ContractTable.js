import React from 'react';
import PriceTickingCell from './PriceTickingCell'
import { secondsToDate } from '../utils';
import { connect } from 'react-redux';
import { getContractsByLeague } from '../selectors'
import { ADD_INSTRUMENT } from '../actions/MarketDataActions';
class ContractTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            highlightedIsin: 0
        }
    }

    render(props) {
        return (
            <table>
                <tbody>
                    <tr>
                        <th>isin</th>
                        <th>home</th>
                        <th>away</th>
                        <th>start</th>
                        <th>bid</th>
                        <th>ask</th>
                        <th>volume</th>
                        <th className='absorbing-column'>last_price</th>
                    </tr>
                    {this
                        .props
                        .contracts
                        .map(instrument => {
                            const isin = instrument.getIsin();
                            return (<tr
                                className={
                                    this.state.highlightedIsin == isin
                                        ? "highlighted-row clickable"
                                        : "clickable"
                                }
                                key={isin}
                                onClick={() => {
                                    this.setState({ highlightedIsin: isin });
                                    this
                                        .props
                                        .handleInstrumentSelection(instrument)
                                }}>
                                <td>{isin}</td>
                                <td>{instrument.getDescription().split(" at ")[0]}</td>
                                <td>{instrument.getDescription().split(" at ")[1]}</td>
                                <td>{secondsToDate(instrument.getStartTimeEpochSeconds()).toLocaleString()}</td>
                                <td>{}</td>
                                <td>{}</td>
                                <td>{}</td>
                                <td><PriceTickingCell price={this.props.prices[isin]} /></td>
                            </tr>);
                        })
                    }
                </tbody>
            </table >
        );
    }

}
/**getContractsByLeague(state, ownProps.league), **/
const mapStateToProps = (state, ownProps) => ({
    contracts: getContractsByLeague(state, ownProps.league),
    prices: state.marketData.pricesByIsin
})

export default connect(mapStateToProps)(ContractTable);