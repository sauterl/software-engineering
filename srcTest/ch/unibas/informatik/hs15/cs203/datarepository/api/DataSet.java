package ch.unibas.informatik.hs15.cs203.datarepository.api;

class DataSet
{
  private MetaData _metaData;
  private String[] _pathsAndContents;

  DataSet(MetaData metaData, String... pathsAndContents)
  {
    this._metaData = metaData;
    this._pathsAndContents = pathsAndContents;
  }

  MetaData getMetaData()
  {
    return _metaData;
  }

  String[] getPathsAndContents()
  {
    return _pathsAndContents;
  }
}
