import React from 'react';
import PriceTickingCell from './PriceTickingCell'

class ContractTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = { highlightedContractId: 0 }
    }


    render(props) {
        return (
            <table>
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
                {this.props.contracts.map(c =>
                    <tr style={{ backgroundColor: this.state.highlightedContractId == c.id ? "yellow" : null }} key={c.id} onClick={() => {
                        this.setState({ highlightedContractId: c.id });
                        this.props.handleClick(c)
                    }}>
                        <td>{c.id}</td>
                        <td>{c.home}</td>
                        <td>{c.away}</td>
                        <td>{c.start}</td>
                        <td>{c.bid}</td>
                        <td>{c.ask}</td>
                        <td>{c.volume}</td>
                        <td><PriceTickingCell price={c.last_price}/></td>
                    </tr>)}
            </table>);
    }

}

export default ContractTable;