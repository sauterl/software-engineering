/*
 * Copyright 2015 Franz-Josef Elmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.unibas.informatik.hs15.cs203.datarepository.api;

import java.util.Date;

/**
 * Criteria to specify a set of data sets. A data set fulfills the criteria if
 * the following criteria are fulfilled if specified (i.e. not <code>null</code> ):
 * <ul>
 * <li>Data set identifier is as the specified <code>id</code>.
 * <li>Data set name is as the specified <code>name</code>.
 * <li>Data set name or description contains the specified <code>text</code>.
 * <li>Data set time stamp is after the specified <code>after</code>.
 * <li>Data set time stamp is before the specified <code>before</code>.
 * </ul>
 * Remarks:
 * <ul>
 * <li>All specified criteria have to be fulfilled.
 * <li>If non of the attributes are specified the set contains all data sets.
 * <li>If <code>id</code> is specified all other attributes have to be <code>null</code>.
 * </ul>
 * 
 * @author Franz-Josef Elmer
 * 
 * @version 2 - modified by loris.sauter: added eclipse generated hash and equals
 */
public final class Criteria
{
  private static final Criteria ALL = new Criteria(null, null, null, null, null);

  /**
   * Returns a criteria which are matched by all data sets.
   */
  public static Criteria all()
  {
    return ALL;
  }

  /**
   * Returns a criteria which can only be matched by the data set with
   * specified id.
   * 
   * @param id
   *          A not-<code>null</code> nonempty string.
   * 
   * @throws IllegalArgumentException
   *           if the argument is <code>null</code> or an empty string.
   */
  public static Criteria forId(String id)
  {
    if (id == null || id.isEmpty())
    {
      throw new IllegalArgumentException("Unspecified id");
    }
    return new Criteria(id, null, null, null, null);
  }

  private final String id;

  private final String name;

  private final String text;

  private final Date before;

  private final Date after;

  /**
   * Creates an instance for specified four criteria. A criterion can be <code>null</code>.
   * 
   * @param nameOrNull
   *          Name data set has to match.
   * @param textOrNull
   *          Text snippet the name or description of a matching data set contains.
   * @param afterOrNull
   *          The time stamp of all matching data sets has to be after this time stamp.
   * @param beforeOrNull
   *          The time stamp of all matching data sets has to be before this time stamp.
   * @throws IllegalArgumentException
   *           if either <code>nameOrNull</code> or <code>textOrNull</code> is an empty string.
   */
  public Criteria(String nameOrNull, String textOrNull, Date afterOrNull,
          Date beforeOrNull)
  {
    this(null, nameOrNull, textOrNull, afterOrNull, beforeOrNull);
  }

  private Criteria(String id, String name, String text, Date after,
          Date before)
  {
    if (name != null && name.isEmpty())
    {
      throw new IllegalArgumentException("Name is an empty string.");
    }
    if (text != null && text.isEmpty())
    {
      throw new IllegalArgumentException("Text snippet is an empty string.");
    }
    this.id = id;
    this.name = name;
    this.text = text;
    this.before = before;
    this.after = after;
  }

  /**
   * Returns the id or <code>null</code> if not specified.
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name or <code>null</code> if not specified.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the text snippet or <code>null</code> if not specified.
   */
  public String getText()
  {
    return text;
  }

  /**
   * Returns the before time stamp or <code>null</code> if not specified.
   */
  public Date getBefore()
  {
    return before;
  }

  /**
   * Returns the after time stamp or <code>null</code> if not specified.
   */
  public Date getAfter()
  {
    return after;
  }

/**
 * Returns a hash value for this object.
 * The hash value method is eclipse generated.
 * @return A hash value for this object.
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((after == null) ? 0 : after.hashCode());
	result = prime * result + ((before == null) ? 0 : before.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((text == null) ? 0 : text.hashCode());
	return result;
}

/**
 * Compares this object and another one on equality.
 * Eclipse generated.
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (!(obj instanceof Criteria))
		return false;
	Criteria other = (Criteria) obj;
	if (after == null) {
		if (other.after != null)
			return false;
	} else if (!after.equals(other.after))
		return false;
	if (before == null) {
		if (other.before != null)
			return false;
	} else if (!before.equals(other.before))
		return false;
	if (id == null) {
		if (other.id != null)
			return false;
	} else if (!id.equals(other.id))
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	if (text == null) {
		if (other.text != null)
			return false;
	} else if (!text.equals(other.text))
		return false;
	return true;
}
}
