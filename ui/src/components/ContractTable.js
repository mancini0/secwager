import React from 'react';
import PriceTickingCell from './PriceTickingCell'

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
                        <th>last_price</th>
                    </tr>
                    {Object.keys(this
                        .props
                        .contracts)
                        .map(isin => {
                            const instrument = this.props.contracts[isin];
                            return (<tr
                                style={{
                                    backgroundColor: this.state.highlightedIsin == isin
                                        ? "yellow"
                                        : null
                                }}
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
                                <td>{instrument.getStartTimeEpochSeconds()}</td>
                                <td>{}</td>
                                <td>{}</td>
                                <td>{}</td>
                                <td><PriceTickingCell price={50.0} /></td>
                            </tr>);
                        })
                    }
                </tbody>
            </table>
        );
    }

}

export default ContractTable;