import React from 'react';
import PriceTickingCell from './PriceTickingCell'
import { secondsToDate } from '../utils';
import { connect } from 'react-redux';
import { getContractsByLeague } from '../selectors'
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
                        <th>id</th>
                        <th>home</th>
                        <th>away</th>
                        <th>start</th>
                        <th>bid</th>
                        <th>ask</th>
                        <th>volume</th>
                        <th className='absorbing-column'>last_price</th>
                    </tr>
                    {Object.keys(this
                        .props
                        .contracts)
                        .map(isin => {
                            const instrument = this.props.contracts[isin];
                            return (<tr

                                className={this.state.highlightedIsin == isin
                                    ? "highlighted-row clickable"
                                    : "clickable"}
                                key={isin}
                                onClick={() => {
                                    this.setState({ highlightedIsin: isin });
                                    this
                                        .props
                                        .handleClick(c)
                                }}>
                                <td>{isin}</td>
                                <td>{instrument.getDescription()}</td>
                                <td>{instrument.getDescription()}</td>
                                <td>{secondsToDate(instrument.getStartTimeEpochSeconds()).toLocaleString()}</td>
                                <td>{}</td>
                                <td>{}</td>
                                <td>{}</td>
                                <td><PriceTickingCell price={this.props.prices[isin]} /></td>
                            </tr>);
                        })
                    }
                </tbody>
            </table>
        );
    }

}
/**getContractsByLeague(state, ownProps.league), **/
const mapStateToProps = (state, ownProps) => ({
    contracts: state.marketData.instrumentsByIsin,
    prices: state.marketData.pricesByIsin
})

export default connect(mapStateToProps)(ContractTable);