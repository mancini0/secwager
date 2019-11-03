import React from 'react';
import Header from './Header';
import LeagueMenu from './LeagueMenu';
import DepthBook from './DepthBook';
import OrderEntry from './OrderEntry';



class TraderDashboard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      selectedInstrument: undefined
    };
  }


  handleInstrumentSelection = (selectedInstrument) => {
    console.log("selectedInstrument:" + JSON.stringify(selectedInstrument))
    console.log("isin:" + JSON.stringify(selectedInstrument.getIsin()));
    this.setState({ selectedInstrument });
  }


  render = (props) => (
    <React.Fragment>
      <div className='row' style={{
        "height": "50px"
      }}>
        <Header />
      </div>
      <div className='row' style={{
        "height": "250px"
      }}>
        < div className='col' style={{
          "backgroundColor": "lightgrey"
        }}>
          <LeagueMenu handleInstrumentSelection={this.handleInstrumentSelection} />
        </div>
      </div>
      {this.state.selectedInstrument &&
        < div className='row' style={{
          "backgroundColor": "gold"
        }}>
          <OrderEntry instrument={this.state.selectedInstrument} />
        </ div>
      }
    </React.Fragment>
  );

}

export default TraderDashboard;